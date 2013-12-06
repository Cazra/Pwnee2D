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
 * A tree-based sorted list implemented as a Red-Black tree.
 * Duplicates are supported and are added towards the right.
 */
public class BalancedBinaryTree<E extends Comparable> {
  
  public static final boolean BLACK = false;
  
  public static final boolean RED = true;
  
  /** Used to help with removing black leaves from the tree. */
  private static final Object curse = new Object();
  
  
  /** The root node of the tree. */
  private RBTNode<E> root;
  
  /** The size of the tree. */
  private int size;
  
  /** The Comparator used to sort the tree's elements. */
  private Comparator<E> comparator;
  
  /** Creates a tree that sorts its elements according to their natural ordering. */
  public BalancedBinaryTree() {
    this(null);
  }
  
  /** Creates a tree that sorts its elements according to some Comparator. */
  public BalancedBinaryTree(Comparator<E> comparator) {
    this.root = null;
    this.size = 0;
    this.comparator = comparator;
  }
  
  
  
  
  public int size() {
    return size;
  }
  
  
  
  //////// Insertion
  
  
  /** 
   * Inserts an element into the tree. 
   * This completes in O(logN) time.
   */
  public void add(E element) {
    if(size == 0) {
      root = new RBTNode(element, BLACK);
    }
    else {
      root = _addRec(element, root);
      root.color = BLACK;
    }
    
    size++;
  }
  
  
  /** 
   * Recursively inserts an element into a subtree while also 
   * balancing the tree. 
   * Returns the new root of the subtree.
   */
  private RBTNode _addRec(E element, RBTNode<E> subroot) {
    
    // Decide whether to add the element to the left or right of the subroot. 
    int compare = compare(element, subroot.value);
    
    if(compare < 0) { // go left
      if(subroot.left == null) {
        RBTNode<E> newNode = new RBTNode<>(element, RED);
        subroot.setLeft(newNode);
        return subroot;
      }
      else {
        subroot.setLeft(_addRec(element, subroot.left));
        return transformAddLeft(subroot);
      }
    }
    else { // go right
      if(subroot.right == null) {
        RBTNode<E> newNode = new RBTNode<>(element, RED);
        subroot.setRight(newNode);
        return subroot;
      }
      else {
        subroot.setRight(_addRec(element, subroot.right));
        return transformAddRight(subroot);
      }
    }
  }
  
  
  
  
  //////// Tree balancing
  
  /** Balances a subtree around its left side. Returns the new subroot. */
  private RBTNode transformAddLeft(RBTNode<E> subroot) {
    RBTNode<E> left = subroot.left;
    if(subroot.color == BLACK && left != null && left.color == RED) {
      if(left.left != null && left.left.color == RED) {
        RBTNode<E> newSubroot = left;
        
        RBTNode<E> newLeft = left.left;
        RBTNode<E> newRight = subroot;
        
        RBTNode<E> newLeftLeft = left.left.left;
        RBTNode<E> newLeftRight = left.left.right;
        
        RBTNode<E> newRightLeft = left.right;
        RBTNode<E> newRightRight = subroot.right;
        
        newSubroot.setLeft(newLeft);
        newSubroot.setRight(newRight);
        
        newLeft.setLeft(newLeftLeft);
        newLeft.setRight(newLeftRight);
        
        newRight.setLeft(newRightLeft);
        newRight.setRight(newRightRight);
        
        newSubroot.color = RED;
        newLeft.color = BLACK;
        newRight.color = BLACK;
        return newSubroot;
      }
      else if(left.right != null && left.right.color == RED) {
        RBTNode<E> newSubroot = left.right;
        
        RBTNode<E> newLeft = left;
        RBTNode<E> newRight = subroot;
        
        RBTNode<E> newLeftLeft = left.left;
        RBTNode<E> newLeftRight = left.right.left;
        
        RBTNode<E> newRightLeft = left.right.right;
        RBTNode<E> newRightRight = subroot.right;
        
        newSubroot.setLeft(newLeft);
        newSubroot.setRight(newRight);
        
        newLeft.setLeft(newLeftLeft);
        newLeft.setRight(newLeftRight);
        
        newRight.setLeft(newRightLeft);
        newRight.setRight(newRightRight);
        
        newSubroot.color = RED;
        newLeft.color = BLACK;
        newRight.color = BLACK;
        return newSubroot;
      }
      else {
        return subroot;
      }
    }
    else {
      return subroot;
    }
  }
  
  /** Balances a subtree around its right side. */
  private RBTNode transformAddRight(RBTNode<E> subroot) {
    RBTNode<E> right = subroot.right;
    if(subroot.color == BLACK && right != null && right.color == RED) {
      if(right.left != null && right.left.color == RED) {
        RBTNode<E> newSubroot = right.left;
        
        RBTNode<E> newLeft = subroot;
        RBTNode<E> newRight = right;
        
        RBTNode<E> newLeftLeft = subroot.left;
        RBTNode<E> newLeftRight = right.left.left;
        
        RBTNode<E> newRightLeft = right.left.right;
        RBTNode<E> newRightRight = right.right;
        
        newSubroot.setLeft(newLeft);
        newSubroot.setRight(newRight);
        
        newLeft.setLeft(newLeftLeft);
        newLeft.setRight(newLeftRight);
        
        newRight.setLeft(newRightLeft);
        newRight.setRight(newRightRight);
        
        newSubroot.color = RED;
        newLeft.color = BLACK;
        newRight.color = BLACK;
        return newSubroot;
      }
      else if(right.right != null && right.right.color == RED) {
        RBTNode<E> newSubroot = right;
        
        RBTNode<E> newLeft = subroot;
        RBTNode<E> newRight = right.right;
        
        RBTNode<E> newLeftLeft = subroot.left;
        RBTNode<E> newLeftRight = right.left;
        
        RBTNode<E> newRightLeft = right.right.left;
        RBTNode<E> newRightRight = right.right.right;
        
        newSubroot.setLeft(newLeft);
        newSubroot.setRight(newRight);
        
        newLeft.setLeft(newLeftLeft);
        newLeft.setRight(newLeftRight);
        
        newRight.setLeft(newRightLeft);
        newRight.setRight(newRightRight);
        
        newSubroot.color = RED;
        newLeft.color = BLACK;
        newRight.color = BLACK;
        return newSubroot;
      }
      else {
        return subroot;
      }
    }
    else {
      return subroot;
    }
  }
  
  
  //////// Removal
  
  /** Empties the tree. This completes in O(1) time. */
  public void clear() {
    root = null;
    size = 0;
  }
  
  
  /** Removes an element from the tree. Completes in O(logN) time. */
  public boolean remove(E element) {
    if(size == 1) {
      if(compare(root.value, element) == 0) {
        root = null;
        size--;
        return true;
      }
      else {
        return false;
      }
    }
    else if(_removeRec(element, root)) {
      root.color = BLACK;
      size--;
      return true;
    } 
    else {
      return false;
    }
  }
  
  
  /** 
   * Recursively removes the element from the tree and rebalances the tree. 
   * Returns true if the element was successfully found and removed.
   */
  private boolean _removeRec(E element, RBTNode<E> subroot) {
    if(subroot == null) {
      return false;
    }
    else {
      int compare = compare(element, subroot.value);
      
      if(compare == 0) { // found leftmost instance? 
        RBTNode<E> prev = _findPrev(subroot);
        if(prev == null || compare(prev.value, element) < 0) { // stop here and remove.
          _removeNode(subroot);
          return true;
        }
        else { // not the leftmost instance. Go left.
          return _removeRec(element, prev);
        }
      }
      else if (compare < 0) { // go left
        return _removeRec(element, subroot.left);
      }
      else { // go right
        return _removeRec(element, subroot.right);
      }
    }
  }
  
  
  /** 
   * Removes a subroot node from the tree and rebalances the tree. 
   * Returns the new subroot.
   */
  private void _removeNode(RBTNode<E> node) {
    RBTNode<E> parent = node.parent;
    RBTNode<E> left = node.left;
    RBTNode<E> right = node.right;
    
    // For the case where we have left and right children, 
    // replace the subroot's value with the value of the previous in-order node
    // and then remove that previous node instead.
    if(left != null && right != null) {
      
      RBTNode<E> prev = _findPrev(node);
      node.value = prev.value;
      _removeNode(prev);
    }
    
    // Cases with only 1 child.
    else if(left != null) { // Node only has left child, which must be red.
      left.color = BLACK;
      _replaceWith(node, left);
    }
    else if(right != null) { // Node only has right child, which must be red.
      right.color = BLACK;
      _replaceWith(node, right);
    }
    
    // Leaf cases
    else if(node.color == RED) { // Node is a red leaf. 
      if(parent == null) {
        root = null;
      }
      else {
        node.remove();
      }
    }
    else { // Node is a black leaf. Removing this unbalances the tree. :(
      _doubleBlack(node);
      node.remove();
    }
  }
  
  
  /** 
   * Adjusts the blackness of the tree to make up for a black leaf that was removed. 
   * Returns the new subroot.
   */
  private void _doubleBlack(RBTNode<E> node) {
    
    RBTNode<E> parent = node.parent;
    if(parent == null) {
      node.color = BLACK;
      root = node;
    }
    else {
      RBTNode<E> sibling = node.getSibling();
      boolean topColor = parent.color;
      RBTNode<E> grandparent = parent.parent;
      boolean isLeft = parent.isLeftChild();
      
      // Case 1: black sibling with at least 1 red child. Restructure.
      if(sibling.color == BLACK && sibling.countRedChildren() >= 1) {
        RBTNode<E> newsubroot;
        
        if(sibling.isLeftChild()) {
          if(sibling.left != null && sibling.left.color == RED) {
            RBTNode<E> red = sibling.left;
            newsubroot = parent.rotateRight();
            
            sibling.color = topColor;
            red.color = BLACK;
            parent.color = BLACK;
          }
          else {
            RBTNode<E> red = sibling.right;
            parent.setLeft(sibling.rotateLeft());
            newsubroot = parent.rotateRight();
            
            red.color = topColor;
            parent.color = BLACK;
          }
        }
        else {
          if(sibling.right != null && sibling.right.color == RED) {
            RBTNode<E> red = sibling.right;
            newsubroot = parent.rotateLeft();
            
            sibling.color = topColor;
            red.color = BLACK;
            parent.color = BLACK;
          }
          else {
            RBTNode<E> red = sibling.left;
            parent.setRight(sibling.rotateRight());
            newsubroot = parent.rotateLeft();
            
            red.color = topColor;
            parent.color = BLACK;
          }
        }
        
        if(grandparent == null) {
          newsubroot.remove();
          root = newsubroot;
        }
        else {
          grandparent.setChild(newsubroot, isLeft);
        }
        
      } // end case 1
      
      // Case 2: black sibling with black children. Recolor.
      else if(sibling.color == BLACK) {
        sibling.color = RED;
        if(parent.color == RED) { // red parent becomes black.
          parent.color = BLACK;
        }
        else {
          _doubleBlack(parent); // parent becomes double-black.
        }
      }
      
      // Case 3: red sibling
      else {
        RBTNode<E> newsubroot;
        RBTNode<E> sibLeft = sibling.left;
        RBTNode<E> sibRight = sibling.right;
        
        // rotate
        if(sibling.isLeftChild()) {
          newsubroot = parent.rotateRight();
        }
        else {
          newsubroot = parent.rotateLeft();
        }
        
        // recolor
        sibling.color = topColor;
        parent.color = RED;
        if(sibLeft != null) {
          sibLeft.color = BLACK;
        }
        if(sibRight != null) {
          sibRight.color = BLACK;
        }
        
        // reassign the subroot.
        if(grandparent == null) {
          newsubroot.remove();
          root = newsubroot;
        }
        else {
          grandparent.setChild(newsubroot, isLeft);
        }
        
        // Apply one of the other cases.
        _doubleBlack(node);
      }
    }
  }
  
  
  
  
  /** Node disconnects from its parent and another node reconnects with the parent in its place. */
  private void _replaceWith(RBTNode<E> node, RBTNode<E> other) {
    RBTNode<E> parent = node.parent;
    
    if(parent == null) {
      node.remove();
      setRoot(other);
    }
    else {
      boolean isLeft = node.isLeftChild();
      node.remove();
      parent.setChild(other, isLeft);
    }
  }
  
  
  private void setRoot(RBTNode<E> node) {
    root = node;
    root.remove();
  }
  
  //////// Find nodes in-order
  
  /** 
   * Finds the node that comes before the given node, in order. 
   * That is, the rightmost node in its left branch.
   */
  private RBTNode<E> _findPrev(RBTNode<E> node) {
    RBTNode<E> prev = node.left;
    if(prev == null) {
      return null;
    }
    
    while(prev.right != null) {
      prev = prev.right;
    }
    
    return prev;
  }
  
  
  //////// Comparison of elements.
  
  /** 
   * Compares two elements using the Comparator, or by their natural ordering 
   * if there is no Comparator. 
   */
  private int compare(E e1, E e2) {
    if(comparator == null) {
      return e1.compareTo(e2);
    }
    else {
      return comparator.compare(e1, e2);
    }
  }
  
  //////// Rendering
  
  public String toString() {
    return "BalancedBinaryTree[" + root + "]";
  }
  
  
  /////// Testing
  
  public static BalancedBinaryTree<Double> randomTree() {
    BalancedBinaryTree<Double> tree = new BalancedBinaryTree<>();
    for(int i = 0; i < 10; i++) {
      tree.add((double) (new java.util.Random()).nextInt(100));
    }
    return tree;
  }
}


/** A node in a red-black binary tree. */
class RBTNode<E extends Comparable> {

  public boolean color;
  
  public E value;
  
  public RBTNode<E> parent;
  
  public RBTNode<E> left;
  
  public RBTNode<E> right;
  
  
  public RBTNode(E value, boolean color) {
    this.value = value;
    this.color = color;
    
    left = null;
    right = null;
    parent = null;
  }
  
  
  public String toString() {
    String str = "RBTNode[" + value + ", ";
    
    if(color == BalancedBinaryTree.RED) {
      str += "RED, ";
    }
    else {
      str += "BLACK, ";
    }
    
    str += "(" + left + ", " + right + ")]";
    return str;
  }
  
  
  public void setLeft(RBTNode<E> left) {
    // Sever the existing left. 
    if(this.left != null) {
      this.left.remove();
    }
    
    // Connect the new left.
    if(left != null) {
      left.setParent(this);
    }
    this.left = left;
  }
  
  
  
  public void setRight(RBTNode<E> right) {
    // Sever the existing right. 
    if(this.right != null) {
      this.right.remove();
    }
    
    // Connect the new right.
    if(right != null) {
      right.setParent(this);
    }
    this.right = right;
  }
  
  
  public void setChild(RBTNode<E> child, boolean leftElseRight) {
    if(leftElseRight) {
      setLeft(child);
    }
    else {
      setRight(child);
    }
  }
  
  
  public void setParent(RBTNode<E> parent) {
    remove();
    this.parent = parent;
  }
  
  
  
  public RBTNode<E> getSibling() {
    if(isLeftChild()) {
      return parent.right;
    }
    else if(isRightChild()) {
      return parent.left;
    }
    else {
      return null;
    }
  }
  
  
  public boolean isRoot() {
    return (parent == null);
  }
  
  
  /** Is this node the left child of its parent? */
  public boolean isLeftChild() {
    return (parent != null && parent.left == this);
  }
  
  /** Is this node the right child of its parent? */
  public boolean isRightChild() {
    return (parent != null && parent.right == this);
  }
  
  /** Removes this node from its parent. */
  public void remove() {
    if(isLeftChild()) {
      parent.left = null;
    }
    else if(isRightChild()) {
      parent.right = null;
    }
    parent = null;
  }
  
  
  //////// Child color count
  
  /** Returns a count of this node's red children. */
  public int countRedChildren() {
    int result = 0;
    if(left != null && left.color == BalancedBinaryTree.RED) {
      result++;
    }
    if(right != null && right.color == BalancedBinaryTree.RED) {
      result++;
    }
    return result;
  }
  
  /** Returns a count of this node's black children. Null children count as black. */
  public int countBlackChildren() {
    int result = 0;
    if(left == null || left.color == BalancedBinaryTree.BLACK) {
      result++;
    }
    if(right == null || right.color == BalancedBinaryTree.BLACK) {
      result++;
    }
    return result;
  }
  
  
  //////// Subtree rotation
  
  
  /** Rotates the subtree at this node to the left. Returns the new subroot. */
  public RBTNode<E> rotateLeft() {
    RBTNode<E> left = this.left;
    RBTNode<E> right = this.right;
    
    RBTNode<E> rl = null;
    RBTNode<E> rr = null;
    if(right != null) {
      rl = right.left;
      rr = right.right;
    }
    
    this.setRight(rl);
    right.setLeft(this);
    return right;
  }
  
  /** Rotates the subtree at this node to the right. Returns the new subroot. */
  public RBTNode<E>  rotateRight() {
    RBTNode<E> left = this.left;
    RBTNode<E> right = this.right;
    
    RBTNode<E> ll = null;
    RBTNode<E> lr = null;
    if(left != null) {
      ll = left.left;
      lr = left.right;
    }
    
    this.setLeft(lr);
    left.setRight(this);
    return left;
  }
  
}
