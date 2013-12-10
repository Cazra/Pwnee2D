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
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Queue;


/** An implementation of a circular doubly-linked list. */
public class CircularDoublyLinkedList<E> extends AbstractList<E> implements Queue<E> {
  
  /** The first node in the list. */
  private DoubleNode<E> first;
  
  /** The last node in the list. */
  private DoubleNode<E> last;
  
  /** The size of the list. */
  int size;
  
  
  /** Constructs an empty list. */
  public CircularDoublyLinkedList() {
    size = 0;
    first = null;
    last = null;
  }
  
  /** Constructs a list containing the elements of the specified Collection. */
  public CircularDoublyLinkedList(Collection<E> collection) {
    this();
    for(E element : collection) {
      this.add(element);
    }
  }
  
  
  /** Gets the element at the specified index. This index will loop around the circular linked list. */
  public E get(int index) {
    DoubleNode<E> node = getNode(index);
    if(node != null) {
      return node.value;
    }
    else {
      return null;
    }
  }
  
  
  /** Gets the node at the specified index. */
  private DoubleNode<E> getNode(int index) {
    DoubleNode<E> node = first;
    while(index > 0) {
      node = node.next;
      index--;
    }
    while(index < 0) {
      node = node.prev;
      index++;
    }
    return node;
  }
  
  
  /** Get the current size of the list. */
  public int size() {
    return size;
  }
  
  
  /** Sets the element at some index in the list. */
  public E set(int index, E element) {
    DoubleNode<E> node = getNode(index);
    E oldElem = node.value;
    node.value = element;
    
    modCount++;
    return oldElem;
  }
  
  
  /** Appends an element to the end of the list. */
  public boolean add(E element) {
    DoubleNode<E> node = new DoubleNode<>(element);
    if(size == 0) {
      first = node;
      first.setNext(node);
    }
    else {
      last.setNext(node);
      node.setNext(first);
    }
    last = node;
    
    modCount++;
    size++;
    return true;
  }
  
  /** Removes the element at the specified index from the list. */
  public E remove(int index) {
    if(size == 0) {
      return null;
    }
    
    DoubleNode<E> node = getNode(index);
    if(size == 1) {
      first = null;
      last = null;
    }
    else {
      node.remove();
    }
    
    modCount++;
    size--;
    return node.value;
  }
  
  
  /** Returns a list iterator over the elements in this list. (in proper sequence) */
  public ListIterator<E> listIterator() {
    final CircularDoublyLinkedList<E> self = this;
    return new ListIterator<E>() {
      int iterModCount = self.modCount;
      
      DoubleNode<E> cur = null;
      int curIndex = -1;
      
      public void add(E e) {
        throw new UnsupportedOperationException();
      }
      
      public boolean hasNext() {
        if(self.size() == 0) {
          return false;
        }
        else {
          return (cur == null || cur.next != self.first);
        }
      }
      
      public boolean hasPrevious() {
        if(self.size() == 0 || cur == null) {
          return false;
        }
        else {
          return (cur.prev != self.last);
        }
      }
      
      public E next() {
        if(iterModCount != self.modCount) {
          throw new ConcurrentModificationException();
        }
        
        if(hasNext()) {
          if(cur == null) {
            cur = self.first;
          }
          else {
            cur = cur.next;
          }
          curIndex++;
          return cur.value;
        }
        else {
          throw new NoSuchElementException();
        }
      }
      
      
      public int nextIndex() {
        return curIndex+1;
      }
      
      public E previous() {
        if(iterModCount != self.modCount) {
          throw new ConcurrentModificationException();
        }
        
        if(hasPrevious()) {
          cur = cur.prev;
          curIndex--;
          return cur.value;
        }
        else {
          throw new NoSuchElementException();
        }
      }
      
      public int previousIndex() {
        return curIndex - 1;
      }
      
      public void remove() {
        throw new UnsupportedOperationException();
      }
      
      
      public void set(E e) {
        throw new UnsupportedOperationException();
      }
    };
  }
  
  
  public ListIterator<E> listIterator(int index) {
    ListIterator<E> iter = listIterator();
    for(int i = 0; i < index; i++) {
      iter.next();
    }
    return iter;
  }
  
  
  public Iterator<E> iterator() {
    return listIterator();
  }
  
  
  //////// Queue implementation
  
  /** Retrieves, but does not remove, the head of this queue.*/
  public E element() {
    if(size() == 0) {
      throw new NoSuchElementException();
    }
    else {
      return get(0);
    }
  }
  
  public boolean offer(E element) {
    return add(element);
  }
  
  public E peek() {
    return get(0);
  }
  
  public E poll() {
    return remove(0);
  }
  
  public E remove() {
    if(size() == 0) {
      throw new NoSuchElementException();
    }
    else {
      return remove(0);
    }
  }
}



/** A node in a doubly-linked list. */
class DoubleNode<E> {
  
  E value;
  
  DoubleNode<E> prev;
  
  DoubleNode<E> next;
  
  
  public DoubleNode(E value) {
    this.value = value;
  }
  
  
  public E getValue() {
    return value;
  }
  
  public DoubleNode<E> getPrev() {
    return prev;
  }
  
  public DoubleNode<E> getNext() {
    return next;
  }
  
  
  
  public void setPrev(DoubleNode<E> node) {
    this.prev = node;
    node.next = this;
  }
  
  
  public void setNext(DoubleNode<E> node) {
    this.next = node;
    node.prev = this;
  }
  
  
  public void remove() {
    this.prev.next = this.next;
    this.next.prev = this.prev;
  }
}






