
public class AVL<Key extends Comparable<Key>>
{
	private Node<Key> root;	//The root node of the Tree
	
	//Default constructor which creates an empty tree
	public AVL()
	{
		root = null;
	}
	
	//Constructor which creates a tree holding one value
	public AVL(Key k)
	{
		root = new Node<Key>(k);
	}
	
	//inserts k into the BST at the appropriate spot
	public void insert(Key k)
	{	
		//If the tree is empty, then insert item at the root
		if(root == null)
		{
			root = new Node<Key>(k);
			return;
		}
		
		Node<Key> current = root;
		Node<Key> previous = null;
		
		//The loop continues until an empty Node is found in which to place the key
		while(current != null)
		{
			int comparison = (k.compareTo((Key)current.key) );
			
			//If the new value is larger than the Node value, go to the right child
			//If smaller, go to the left child
			//If equal, then the key already exists, so do not insert it again
			if(comparison > 0)
			{
				previous = current;
				current = current.rightChild;
			}
			else if(comparison < 0)
			{
				previous = current;
				current = current.leftChild;
			}
			else
			{
				return;
			}
		}//end while
		
		//When we find the right spot, the current Node will be null
		//Use the previous Node to create the association to the new Node
		Node<Key> newNode = new Node<Key>(k, null, null, previous);
		if(k.compareTo( (Key)previous.key ) > 0)
		{
			previous.rightChild = newNode;
		}
		else
		{
			previous.leftChild = newNode;
		}

		rebalance(newNode);
	}//end insert
	
	//removes k from the BST if k exists
	//returns false if k is not in the tree
	public boolean delete(Key k)
	{
		if(root == null)
		{
			return false;
		}
		
		Node<Key> current = root;
		boolean isRightChild = false;	//Whether current is the left or right child of its parent node
		
		//The loop continues until k is found or there are no more Nodes to check
		while(current != null)
		{
			int comparison = (k.compareTo( (Key)current.key) );
			
			//If the new value is larger than the Node value, go to the right child
			//If smaller, go to the left child
			//If equal, then the key is located, then end the loop
			if(comparison > 0)
			{
				isRightChild = true;
				current = current.rightChild;
			}
			else if(comparison < 0)
			{
				isRightChild = false;
				current = current.leftChild;
			}
			else
			{
				break;
			}
		}//end while
		
		//If k does not exist in the tree, return false
		if(current == null)
		{
			return false;
		}
		//If k exists, delete the node containing k
		else
		{
			Node<Key> leftChild = current.leftChild;
			Node<Key> rightChild = current.rightChild;
			
			//case one: the node containing k has no children
			if(leftChild == null && rightChild == null)
			{
				if(isRightChild)
				{
					current.parent.rightChild = null;
				}
				else
				{
					current.parent.leftChild = null;
				}
			}
			//case two: the node containing k has one child
			else if(leftChild == null)
			{	
				current = current.parent;
				
				//Point around the deleted node
				rightChild.parent = current;
				if(isRightChild)
				{
					current.rightChild = rightChild;
				}
				else
				{
					current.leftChild = rightChild;
				}
			}
			else if(rightChild == null)
			{
				current = current.parent;
				
				//Point around the deleted node
				leftChild.parent = current;
				if(isRightChild)
				{
					current.rightChild = leftChild;
				}
				else
				{
					current.leftChild = leftChild;
				}
			}
			//case three: the node containing k has two children
			else
			{
				//find an appropriate replacement --> the leftmost node of right subtree
				isRightChild = true;
				Node<Key> target = current.rightChild;
				
				while(target.leftChild != null)
				{
					target = target.leftChild;
					isRightChild = false;
				}
				current.key = target.key;	//swap value into the "deleted" node
				
				//Remove the leftmost node
				if(isRightChild)
				{
					current = target.parent;	//for rebalance() call
					target.parent.rightChild = null;
				}
				else
				{
					current = target.parent;	//for rebalance() call
					target.parent.leftChild = null;
				}
			}
			
			//reblance from top to bottom
			rebalance(current);
			return true;
		}		
	}//end delete
	
	//Checks whether the tree is balanced, starting at Node n
	//If unbalanced, rebalances the subtree
	private void rebalance(Node<Key> node)
	{
		while(node != null)
		{
			int lC = 0;
			int rC = 0;
			
			if(node.leftChild != null)
			{
				lC = node.leftChild.depth;
			}//end if
			if(node.rightChild != null)
			{
				rC = node.rightChild.depth;
			}//end if
			
			node.depth = (lC < rC ? rC : lC) + 1;

			//if left subtree is deeper than right
			if(lC - rC > 1)
			{
				
				int leftLeftDepth = 0;
				if(node.leftChild.leftChild != null)
				{
					leftLeftDepth = node.leftChild.leftChild.depth;
				}
				
				int leftRightDepth = 0;
				if(node.leftChild.rightChild != null)
				{
					leftRightDepth = node.leftChild.rightChild.depth;
				}
				
				
				//If the node's left child's left subtree is deeper than its right subtree
				//Do a right rotation
				if(leftLeftDepth > leftRightDepth)
				{
					node = rotateRight(node);
				}
				//Else the opposite is true --> right is deeper than left
				else
				{
					node = rotateLeftRight(node);
				}
			}
			else if(rC - lC > 1)
			{
				int rightRightDepth = 0;
				if(node.rightChild.rightChild != null)
				{
					rightRightDepth = node.rightChild.rightChild.depth;
				}
				
				int rightLeftDepth = 0;
				if(node.rightChild.leftChild != null)
				{
					rightLeftDepth = node.rightChild.leftChild.depth;
				}
				
				//If the node's right child's right subtree is deeper than its left subtree
				//Do a left rotation
				if(rightRightDepth > rightLeftDepth)
				{
					node = rotateLeft(node);
				}
				//Else the opposite is true --> right is deeper than left
				else
				{
					node = rotateRightLeft(node);
				}
			}//end if
			if(node.parent == null)
			{
				root = node;
			}

			node = node.parent;
		}//end while

	}
	
	//sets the depth of the node based on the depths of its children nodes
	private void updateDepth(Node<Key> node)
	{
		int lC = 0;
		int rC = 0;
		
		if(node.leftChild != null)
		{
			lC = node.leftChild.depth;
		}//end if
		if(node.rightChild != null)
		{
			rC = node.rightChild.depth;
		}//end if
		
		node.depth = (lC < rC ? rC : lC) + 1;
	}
	
	//Takes in the grandparent node, and rotates the subtree left
	private Node<Key> rotateLeft(Node<Key> node)
	{
		Node<Key> median = node.rightChild;
		node.rightChild = median.leftChild;
		
		//If median's left child was not null, then set node as its new parent
		if(node.rightChild != null)
		{
			node.rightChild.parent = node;
		}
		
		median.leftChild = node;	//makes median the new "root" in a left-left pattern
		
		//make sure parent references are updated
		median.parent = node.parent;	
		node.parent = median;
		//If median's parent is a node, then make sure it points to median as its child
		if(median.parent != null)
		{
			//find which child node was, and set the parent's old reference to median instead
			if(median.parent.rightChild == node)	
			{
				median.parent.rightChild = median;
			}
			else
			{
				median.parent.leftChild = median;
			}
		}
		updateDepth(node);
		updateDepth(median);
		
		return median;
	}//end rotateLeft
	
	//Takes in the grandparent node, and rotates the subtree
	private Node<Key> rotateRight(Node<Key> node)
	{
		Node<Key> median = node.leftChild;
		node.leftChild = median.rightChild;
		
		if(node.leftChild != null)
		{
			node.leftChild.parent = node;
		}
		median.rightChild = node;
		
		
		median.parent = node.parent;
		node.parent = median;
		if(median.parent != null)
		{
			if(median.parent.rightChild == node)
			{
				median.parent.rightChild = median;
			}
			else
			{
				median.parent.leftChild = median;
			}
		}
		
		updateDepth(node);
		updateDepth(median);
		
		return median;
	}//end rotateRight
	
	//Turns the subtree into a right-right pattern
	//Then calls left rotate to complete the rotation
	private Node<Key> rotateRightLeft(Node<Key> node)
	{
		node.rightChild = rotateRight(node.rightChild);
		return rotateLeft(node);
	}
	
	//Turns the subtree into a left-left pattern
	//Then calls right rotate to complete the rotation
	private Node<Key> rotateLeftRight(Node<Key> node)
	{
		node.leftChild = rotateLeft(node.leftChild);
		return rotateRight(node);
	}
	
	//returns true if k is in the BST
	//else returns false
	public boolean contains(Key k)
	{
		if(root == null)
		{
			return false;
		}
		
		Node<Key> current = root;
		
		//The loop continues until there are no more Nodes to check in the tree
		//If the loop ends, then k does not exist in the tree
		while(current != null)
		{
			int comparison = (k.compareTo( (Key)current.key) );
			
			//If the new value is larger than the Node value, go to the right child
			//If smaller, go to the left child
			//If equal, then the key already exists, so do not insert it again
			if(comparison > 0)
			{
				current = current.rightChild;
			}
			else if(comparison < 0)
			{
				current = current.leftChild;
			}
			else
			{
				return true;
			}
		}//end while
		return false;
	}

	
	//Prints an in-order visit of every node in the BST
	public void inorder()
	{
		System.out.println(inOrderRecursion(this.root));
	}
	
	//Recursively visits the tree in a left-subtree, self, right-subtree pattern
	//and returns a String containing the contents of the tree
	private String inOrderRecursion(Node<Key> node)
	{
		String s = "";	
		
		//base case = node is null
		if(node != null)
		{
			s += inOrderRecursion(node.leftChild);
			s += (node.key + "(" + node.depth + ") ");
			s += inOrderRecursion(node.rightChild);
		}
		return s;
	}
	
	//Prints a pre-order visit of every node in the BST
	public void preorder()
	{
		System.out.println(preOrderRecursion(this.root));
	}
	
	
	//Recursively visits the tree in a self, left-subtree, right-subtree pattern
	//and returns a String containing the contents of the tree
	private String preOrderRecursion(Node<Key> node)
	{
		String s = "";
		
		if(node != null)
		{
			s += (node.key + " ");
			s += preOrderRecursion(node.leftChild);
			s += preOrderRecursion(node.rightChild);
		}
		return s;
	}
	
	//Prints a post-order visit of every node in the BST
	public void postorder()
	{
		System.out.println(postOrderRecursion(this.root));
	}
	
	//Recursively visits the tree in a left-subtree, right-subtree, self pattern
	//and returns a String containing the contents of the tree
	private String postOrderRecursion(Node<Key> node)
	{
		String s = "";
		
		if(node != null)
		{
			//Concatenate left, right, then parent node items in String s
			s += postOrderRecursion(node.leftChild);
			s += postOrderRecursion(node.rightChild);
			s += (node.key + " ");
		}
		return s;
	}
	
	private class Node<Key extends Comparable<Key>>
	{
		private Node<Key> leftChild;		//The left child of this node
		private Node<Key> rightChild;	//The right child of this node
		private Node<Key> parent;		//The parent of this node
		private Key key;			//The value stored in this node
		private int depth;

		
		//Constructor that takes a key value as a parameter
		//Creates a populated node with no children
		public Node(Key key)
		{
			this.key = key;
			this.leftChild = null;
			this.rightChild = null;
			this.parent = null;
			this.depth = 1;
		}
		
		//Constructor that takes a key, children nodes, and a parent node as parameters
		public Node(Key key, Node<Key> leftChild, Node<Key> rightChild, Node<Key> parent)
		{
			this.key = key;
			this.leftChild = leftChild;
			this.rightChild = rightChild;
			this.parent = parent;
			this.depth = 1;
		}
		
	}
	
}

