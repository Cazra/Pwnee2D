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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;


/** An iterator for an in-order traversal of a binary tree. */
public class BinaryTreeIterator<E> implements Iterator<E> {
  
  /** Code for doing an in-order traversal over the tree. */
  public static final int IN_ORDER = 0;
  
  /** Code for doing a pre-order traversal over the tree. */
  public static final int PRE_ORDER = 1;
  
  /** Code for doing a post-order traversal over the tree. */
  public static final int POST_ORDER = 2;
  
  /** Code for doing a level-order traversal over the tree. */
  public static final int LEVEL_ORDER = 3;
  
  private BinaryTree<E> tree;
  
  private LinkedList<E> elements;
  
  private E lastNext = null;
  
  /** Creates an iterator to traverse over the tree in the specified ordering. */
  public BinaryTreeIterator(BinaryTree<E> tree, int order) {
    this.tree = tree;
    elements = new LinkedList<>();
    
    if(order == IN_ORDER) {
      _inorderTraversal(tree.getRoot());
    }
    else if(order == PRE_ORDER) {
      _preorderTraversal(tree.getRoot());
    }
    else if(order == POST_ORDER) {
      _postorderTraversal(tree.getRoot());
    }
    else if(order == LEVEL_ORDER) {
      _levelorderTraversal(tree.getRoot());
    }
    else {
      throw new RuntimeException("Invalid ordering code for BinaryTreeIterator.");
    }
  }
  
  /** Creates an iterator that does an in-order traversal over the tree. */
  public BinaryTreeIterator(BinaryTree<E> tree) {
    this(tree, IN_ORDER);
  }
  
  private void _inorderTraversal(BinaryTreeNode<E> node) {
    if(node != null) {
      _inorderTraversal(node.getLeft());
      elements.add(node.getValue());
      _inorderTraversal(node.getRight());
    }
  }
  
  private void _preorderTraversal(BinaryTreeNode<E> node) {
    if(node != null) {
      elements.add(node.getValue());
      _preorderTraversal(node.getLeft());
      _preorderTraversal(node.getRight());
    }
  }
  
  private void _postorderTraversal(BinaryTreeNode<E> node) {
    if(node != null) {
      _postorderTraversal(node.getLeft());
      _postorderTraversal(node.getRight());
      elements.add(node.getValue());
    }
  }
  
  private void _levelorderTraversal(BinaryTreeNode<E> node) {
    LinkedList<BinaryTreeNode> queue = new LinkedList<>();
    if(node != null) {
      queue.add(node);
    }
    
    while(!queue.isEmpty()) {
      node = queue.remove();
      
      elements.add(node.getValue());
      
      if(node.getLeft() != null) {
        queue.add(node.getLeft());
      }
      if(node.getRight() != null) {
        queue.add(node.getRight());
      }
    }
  }
  
  
  /** Returns true if the iteration has more elements. */
  public boolean hasNext() {
    return !elements.isEmpty();
  }
  
  /** Returns the next element in the iteration. */
  public E next() {
    if(!hasNext()) {
      throw new NoSuchElementException();
    }
    else {
      lastNext = elements.remove();
      return lastNext;
    }
  }
  
  /** Removes from the underlying collection the last element returned by the iterator. */
  public void remove() {
    if(lastNext == null) {
      throw new IllegalStateException();
    }
    else {
      tree.remove(lastNext);
    }
  }
}


