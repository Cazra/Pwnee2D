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


/** Abstraction of a node in a binary tree. */
public abstract class BinaryTreeNode<E> {
  
  
  /** Returns the element contained at this node. */
  public abstract E getValue();
  
  /** Returns the parent of this node. */
  public abstract BinaryTreeNode<E> getParent();
  
  /** Returns the left child of this node. */
  public abstract BinaryTreeNode<E> getLeft();
  
  /** Returns the right child of this node. */
  public abstract BinaryTreeNode<E> getRight();
  
  
  /** 
   * Returns the next node in an in-order traversal of the node's tree. 
   * Returns null if this is the last in-order node in the tree. 
   */
  public BinaryTreeNode<E> getNext() {
    
    // Case 1: the next node is the leftmost child in this node's right branch.
    BinaryTreeNode<E> next = getRight();
    if(next != null) {
      while(next.getLeft() != null) {
        next = next.getLeft();
      }
      return next;
    }
    
    // Case 2: the next node is the first parent to the right.
    else {
      BinaryTreeNode<E> cur = this;
      next = cur.getParent();
      
      while(next != null) {
        if(next.getLeft() == cur) {
          return next;
        }
        else {
          cur = next;
          next = cur.getParent();
        }
      }
      
      return null;
    }
  }
  
  /** Returns the previous node in an in-order traversal of the node's tree. */
  public BinaryTreeNode<E> getPrev() {
    
    // Case 1: the prev node is the rightmost child in this node's left branch.
    BinaryTreeNode<E> prev = getLeft();
    if(prev != null) {
      while(prev.getRight() != null) {
        prev = prev.getRight();
      }
      return prev;
    }
    
    // Case 2: the prev node is the first parent to the left.
    else {
      BinaryTreeNode<E> cur = this;
      prev = cur.getParent();
      
      while(prev != null) {
        if(prev.getRight() == cur) {
          return prev;
        }
        else {
          cur = prev;
          prev = cur.getParent();
        }
      }
      
      return null;
    }
  }
  
  
  /** Returns true iff this node has no parent. */
  public boolean isRoot() {
    return (getParent() == null);
  }
  
  
  /** Returns true iff this node is the left child of its parent node. */
  public boolean isLeftChild() {
    return (getParent() != null && getParent().getLeft() == this);
  }
  
  /** Returns true iff this node is the right child of its parent node. */
  public boolean isRightChild() {
    return (getParent() != null && getParent().getRight() == this);
  }
  
  
  /** Returns this node's sibling. */
  public BinaryTreeNode<E> getSibling() {
    if(isLeftChild()) {
      return getParent().getRight();
    }
    else if(isRightChild()) {
      return getParent().getLeft();
    }
    else {
      return null;
    }
  }
}

