
//NOTE: Code from the BST is used as a base and may remain unchanged in certain methods


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
			int comparison = (k.compareTo((Key)current.getKey()) );
			
			//If the new value is larger than the Node value, go to the right child
			//If smaller, go to the left child
			//If equal, then the key already exists, so do not insert it again
			if(comparison > 0)
			{
				previous = current;
				current = current.getRightChild();
			}
			else if(comparison < 0)
			{
				previous = current;
				current = current.getLeftChild();
			}
			else
			{
				return;
			}
		}//end while
		
		//When we find the right spot, the current Node will be null
		//Use the previous Node to create the association to the new Node
		if(k.compareTo( (Key)previous.getKey() ) > 0)
		{
			previous.setRightChild(new Node<Key>(k));
		}
		else
		{
			previous.setLeftChild(new Node<Key>(k));
		}
	}//end insert
	
	//removes k from the BST if k exists
	//returns false if k is not in the tree
	public boolean delete(Key k)
	{
		if(root == null)
		{
			return false;
		}
		
		Node<Key> previous = null;
		Node<Key> current = root;
		boolean currentIsRightChild = false;	//Whether current is the left or right child of its parent node
		
		//The loop continues until k is found or there are no more Nodes to check
		while(current != null)
		{
			int comparison = (k.compareTo( (Key)current.getKey()) );
			
			//If the new value is larger than the Node value, go to the right child
			//If smaller, go to the left child
			//If equal, then the key is located, then end the loop
			if(comparison > 0)
			{
				currentIsRightChild = true;
				previous = current;
				current = current.getRightChild();
			}
			else if(comparison < 0)
			{
				currentIsRightChild = false;
				previous = current;
				current = current.getLeftChild();
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
			Node<Key> leftChild = current.getLeftChild();
			Node<Key> rightChild = current.getRightChild();
			
			//case one: the node containing k has no children
			if(leftChild == null && rightChild == null)
			{
				if(currentIsRightChild)
				{
					previous.setRightChild(null);
				}
				else
				{
					previous.setLeftChild(null);
				}
			}
			//case two: the node containing k has one child
			else if(leftChild == null)
			{	
				//Point previous's appropriate child at current's right child
				if(currentIsRightChild)
				{
					previous.setRightChild(rightChild);
				}
				else
				{
					previous.setLeftChild(rightChild);
				}
			}
			else if(rightChild == null)
			{
				//Point previous's appropriate child at current's left child
				if(currentIsRightChild)
				{
					previous.setRightChild(leftChild);
				}
				else
				{
					previous.setLeftChild(leftChild);
				}
			}
			//case three: the node containing k has two children
			else
			{
				//find an appropriate replacement --> the leftmost node of right subtree
				previous = current;
				currentIsRightChild = true;
				Node<Key> target = current.getRightChild();
				
				while(target.getLeftChild() != null)
				{
					previous = target;
					target = target.getLeftChild();
					currentIsRightChild = false;
				}
				current.setKey(target.getKey());
				
				if(currentIsRightChild)
				{
					previous.setRightChild(null);
				}
				else
				{
					previous.setLeftChild(null);
				}
			}
			return true;
		}		
	}//end delete
	
	
	
	
	
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
			int comparison = (k.compareTo( (Key)current.getKey()) );
			
			//If the new value is larger than the Node value, go to the right child
			//If smaller, go to the left child
			//If equal, then the key already exists, so do not insert it again
			if(comparison > 0)
			{
				current = current.getRightChild();
			}
			else if(comparison < 0)
			{
				current = current.getLeftChild();
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
			s += inOrderRecursion(node.getLeftChild());
			s += (node.getKey() + " ");
			s += inOrderRecursion(node.getRightChild());
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
			s += (node.getKey() + " ");
			s += preOrderRecursion(node.getLeftChild());
			s += preOrderRecursion(node.getRightChild());
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
			s += postOrderRecursion(node.getLeftChild());
			s += postOrderRecursion(node.getRightChild());
			s += (node.getKey() + " ");
		}
		return s;
	}
	
	private class Node<Key extends Comparable<Key>>
	{
		private Node<Key> leftChild;		//The left child of this node
		private Node<Key> rightChild;	//The right child of this node
		private Key key;			//The value stored in this node

		
		//Constructor that takes a key value as a parameter
		//Creates a populated node with no children
		public Node(Key key)
		{
			this.key = key;
			this.leftChild = null;
			this.rightChild = null;
		}
		
		//Setters and getters
		public void setLeftChild(Node<Key> leftChild)
		{
			this.leftChild = leftChild;
		}
		
		public void setRightChild(Node<Key> rightChild)
		{
			this.rightChild = rightChild;
		}
		
		public void setKey(Key key)
		{
			this.key = key;
		}
		
		public Node<Key> getLeftChild()
		{
			return this.leftChild;
		}
		
		public Node<Key> getRightChild()
		{
			return this.rightChild;
		}
		
		public Key getKey()
		{
			return this.key;
		}
	}
	
}

