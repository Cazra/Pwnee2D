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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/** An abstraction for a binary tree collection. */
public abstract class BinaryTree<E> implements Collection<E> {
  
  /** Returns the root node of the tree. */
  public abstract BinaryTreeNode<E> getRoot();
  
  /** 
   * Returns the node in the tree containing the specified element, 
   * or null if the element doesn't exist in this tree. 
   */
  public abstract BinaryTreeNode<E> getNode(E element);
  
  
  /** Returns a list of the tree's elements by an in-order traversal. */
  public List<E> toListInorder() {
    Iterator<E> iter = new BinaryTreeIterator(this, BinaryTreeIterator.IN_ORDER);
    List<E> result = new ArrayList<>();
    
    while(iter.hasNext()) {
      result.add(iter.next());
    }
    
    return result;
  }
  
  /** Returns a list of the tree's elements by a pre-order traversal. */
  public List<E> toListPreorder() {
    Iterator<E> iter = new BinaryTreeIterator(this, BinaryTreeIterator.PRE_ORDER);
    List<E> result = new ArrayList<>();
    
    while(iter.hasNext()) {
      result.add(iter.next());
    }
    
    return result;
  }
  
  /** Returns a list of the tree's elements by a post-order traversal. */
  public List<E> toListPostorder() {
    Iterator<E> iter = new BinaryTreeIterator(this, BinaryTreeIterator.POST_ORDER);
    List<E> result = new ArrayList<>();
    
    while(iter.hasNext()) {
      result.add(iter.next());
    }
    
    return result;
  }
  
  /** Returns a list of the tree's elements by a level-order traversal. */
  public List<E> toListLevelorder() {
    Iterator<E> iter = new BinaryTreeIterator(this, BinaryTreeIterator.LEVEL_ORDER);
    List<E> result = new ArrayList<>();
    
    while(iter.hasNext()) {
      result.add(iter.next());
    }
    
    return result;
  }
}

