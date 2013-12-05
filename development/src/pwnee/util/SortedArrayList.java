package pwnee.util;

/*======================================================================
 * 
 * Pwnee - A lightweight 2D Java game engine
 * 
 * Copyright (c) 2012 by Stephen Lindberg (sllindberg21@students.tntech.edu)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: 
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
======================================================================*/

import java.util.AbstractList;
import java.util.Comparator;

/** 
 * An array-based List that inserts its elements sorted. 
 * Duplicates are permitted, with oldest duplicates being towards the front of the list.
 */
public class SortedArrayList<E extends Comparable> extends AbstractList<E> {
  
  private static final int INIT_CAPACITY = 11;
  
  private Object[] list;
  
  private int size;
  
  private Comparator<E> comparator;
  
  /** Produces a list that sorts elements according to their natural order. */
  public SortedArrayList() {
    this(null);
  }
  
  
  /** Produces a list that sorts elements using a given Comparator. */
  public SortedArrayList(Comparator<E> c) {
    super();
    list = new Object[INIT_CAPACITY];
    size = 0;
    comparator = c;
  }
  
  /** O(1) */
  @Override
  public E get(int index) {
    return (E) list[index];
  }
  
  @Override
  public int size() {
    return size;
  }
  
  /** O(1) */
  @Override
  public E set(int index, E element) {
    if(element == null) {
      throw new NullPointerException("SortedArrayList does not permit null elements.");
    }
    if(index < 0 || index >= size) {
      throw new IndexOutOfBoundsException("Index out of bounds at " + index + " with max index " + (size - 1));
    }
    
    E result = get(index);
    list[index] = element;
    
    return result;
  }
  
  /** O(N) due to shifting. */
  @Override
  public void add(int index, E element) {
    // expand our array if we need to.
    if(size == list.length) {
      _expandArray();
    }
    
    // shift elements at or behind index back.
    for(int i = size - 1; i >= index; i--) {
      list[i+1] = list[i];
    }
    size++;
    
    set(index, element);
  }
  
  /** 
   * Contrary to the specification of the List interface, this does not append 
   * the element to the end of the list. 
   * Instead, the element is sort-inserted into the list. 
   * Returns true.
   */
  @Override
  public boolean add(E e) {
    addSorted(e);
    return true;
  }
  
  /** O(N) due to shifting. */
  @Override
  public E remove(int index) {
    E result = get(index);
    
    // shift elements behind index forward.
    size--;
    for(int i = index; i < size; i++) {
      list[i] = list[i+1];
    }
    
    // compress the array if we need to.
    if(size < list.length/2) {
      _compressArray();
    }
    
    return result;
  }

  
  
  //////// Binary search
  
  
  /** 
   * Gets the first index of an element in the list. Returns -1 if the element doesn't exist.
   * O(logN)
   */
  @Override
  public int indexOf(Object o) {
    E e = (E) o;
    
    int min = 0;
    int max = size - 1;
    int i = max/2;
    
    int lowest= -1;
    
    while(min <= max) {
      int compare = compare(e, get(i));
      
      if(compare == 0) {
        if(lowest == -1 || i < lowest) {
          lowest = i;
        }
        max = i - 1;
        i = min + (max - min)/2;
      }
      else if(compare < 0) {
        max = i - 1;
        i = min + (max - min)/2;
      }
      else {
        min = i + 1;
        i = min + (max - min)/2;
      }
    }
    
    return lowest;
  }
  
  
  /** 
   * Gets the last index of an element in the list. Returns -1 if the element doesn't exist.
   * O(logN)
   */
  @Override
  public int lastIndexOf(Object o) {
    E e = (E) o;
    
    int min = 0;
    int max = size - 1;
    int i = max/2;
    
    int highest = -1;
    
    while(min <= max) {
      int compare = compare(e, get(i));
      
      if(compare == 0) {
        if( i > highest) {
          highest = i;
        }
        min = i + 1;
        i = min + (max - min)/2;
      }
      else if(compare < 0) {
        max = i - 1;
        i = min + (max - min)/2;
      }
      else {
        min = i + 1;
        i = min + (max - min)/2;
      }
    }
    
    return highest;
  }
  
  
  /** 
   * Removes the first instance of an element from the list. 
   * Returns true iff the element was found and removed successfully. 
   * O(logN)
   */
  @Override
  public boolean remove(Object o) {
    return (removeFirst((E) o) > -1);
  }
  
  
  //////// Sorted insertions O(logN)
  // O(N) due to shifting.
  
  /** 
   * Adds an element to the list in sorted order. 
   * Duplicates are permitted, with the oldest duplicates being toward the front in the list. 
   * Returns the index at which the element was inserted.
   */
  public int addSorted(E e) {
    if(e == null) {
      throw new NullPointerException("SortedArrayList does not permit null elements.");
    }
    
    // Perform a binary search to find the index at which to insert the element.
    int min = 0;
    int max = size - 1;
    int i = max/2;
    
    while(min < max) {
      if(compare(e, get(i)) < 0) {
        max = i - 1;
        i = min + (max - min)/2;
      }
      else {
        min = i + 1;
        i = min + (max - min)/2;
      }
    }
    if(i < size && compare(e, get(i)) >= 0) {
      i++;
    }
    
    // Add the element at the appropriate index.
    add(i, e);
    return i;
  }
  
  
  /** Compares two elements using the provided comparator or their natural ordering (if the comparator wasn't provided). */
  private int compare(E e1, E e2) {
    if(comparator == null) {
      return e1.compareTo(e2);
    }
    else {
      return comparator.compare(e1, e2);
    }
  }
  
  
  //////// Binary search contains 
  // O(logn)
  
  /** Returns true iff the list contains the specified element. */
  @Override
  public boolean contains(Object o) {
    E e = (E) o;
    
    // Perform a binary search to find the index at which to remove the element.
    int min = 0;
    int max = size - 1;
    int i = max/2;
    
    while(min <= max) {
      int compare = compare(e, get(i));
      
      if(compare == 0) {
        return true;
      }
      else if(compare < 0) {
        max = i - 1;
        i = min + (max - min)/2;
      }
      else {
        min = i + 1;
        i = min + (max - min)/2;
      }
    }
    
    return false;
  }
  
  
  //////// Binary search remove
  // O(N) due to shifting.
  
  /** 
   * Removes the first instance of the given element from the list. 
   * Returns the index of the removed element. If the element could not be found, -1 is returned.
   */
  public int removeFirst(E e) {
    // Perform a binary search to find the index at which to remove the element.
    int index = indexOf(e);
    
    if(index > -1) {
      remove(index);
    }

    return index;
  }
  
  /** 
   * Removes the last instance of the given element from the list. 
   * Returns the index of the removed element. If the element could not be found, -1 is returned.
   */
  public int removeLast(E e) {
    // Perform a binary search to find the index at which to remove the element.
    int index = lastIndexOf(e);
    
    if(index > -1) {
      remove(index);
    }

    return index;
  }
  
  
  //////// Array resizing
  
  /** Double the array's size. */
  private void _expandArray() {
    Object[] result = new Object[list.length * 2];
    for(int i = 0; i < list.length; i++) {
      result[i] = list[i];
    }
    
    list = result;
  }
  
  /** Compress the array to fit the current size. */
  private void _compressArray() {
    if(size > 1) {
      Object[] result = new Object[size];
      for(int i = 0; i < size; i++) {
        result[i] = list[i];
      }
      
      list = result;
    }
  }
  
}
