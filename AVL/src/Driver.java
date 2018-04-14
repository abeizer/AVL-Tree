
public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AVL<Integer> avl = new AVL<>();
		
		avl.insert(50);
		avl.insert(30);
		avl.insert(20);
		avl.insert(1);
		avl.insert(40);
		avl.insert(70);
		avl.insert(60);
		avl.insert(80);
		avl.insert(100);
		avl.inorder();


		avl.delete(50);	avl.inorder();
		avl.delete(40);	avl.inorder();
		avl.delete(30);	avl.inorder();

	}

}
