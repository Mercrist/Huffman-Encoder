package SortedList;


/**
 * Implementation of a SortedList using a SinglyLinkedList
 * @author Fernando J. Bermudez & Juan O. Lopez
 * @author ADD YOUR NAME HERE
 * @version 2.0
 * @since 10/16/2021
 */
public class SortedLinkedList<E extends Comparable<? super E>> extends AbstractSortedList<E> {

	@SuppressWarnings("unused")
	private static class Node<E> {

		private E value;
		private Node<E> next;

		public Node(E value, Node<E> next) {
			this.value = value;
			this.next = next;
		}

		public Node(E value) {
			this(value, null); // Delegate to other constructor
		}

		public Node() {
			this(null, null); // Delegate to other constructor
		}

		public E getValue() {
			return value;
		}

		public void setValue(E value) {
			this.value = value;
		}

		public Node<E> getNext() {
			return next;
		}

		public void setNext(Node<E> next) {
			this.next = next;
		}

		public void clear() {
			value = null;
			next = null;
		}
	} // End of Node class


	private Node<E> head; // First DATA node (This is NOT a dummy header node)

	public SortedLinkedList() {
		head = null;
		currentSize = 0;
	}

	@Override
	public void add(E e) {
		/* TODO ADD CODE HERE */
		/* Special case: Be careful when the new value is the smallest */
		if(currentSize == 0){
			head = new Node<E>(e);

		}
		else if(e.compareTo(head.value) < 0){ //new value smaller than head
			head = new Node<E>(e, head); //value added becomes the new head with a next set to the old head
		}

		else{ //place node where it belongs in order
			Node<E> node = head;
			while(node.next != null && e.compareTo(node.next.value) > 0){ //while e is the biggest value
				node = node.next;
			}
			node.next = new Node<E>(e, node.next);
		}

		currentSize++;

	}

	@Override
	public boolean remove(E e) {
		/* TODO ADD CODE HERE */
		/* Special case: Be careful when the value is found at the head node */
		if(currentSize < 1) throw new ArrayIndexOutOfBoundsException("Can't remove items from an empty list!");

		Node<E> toDelete;
		if(head.value.equals(e)){
			toDelete = head;
			head = head.next;
			toDelete.clear();
			currentSize--;
			return true;
		}

		Node<E> node = head;
		while(node.next != null){
			if(node.next.value.equals(e)){
				toDelete = node.next;
				node.next = node.next.next;
				toDelete.clear();
				currentSize--;
				return true;
			}
			node = node.next;
		}
		return false;
	}

	@Override
	public E removeIndex(int index) { //won't use remove() to reduce time complexity
		/* TODO ADD CODE HERE */
		/* Special case: Be careful when index = 0 */
		if(index < 0 || index > currentSize-1)
			throw new IndexOutOfBoundsException("Can't remove index: " + index);

		Node<E> toDelete;
		E val;
		Node<E> node;
		if(index == 0){
			toDelete = head;
			val = head.value;
			head = head.next;
			toDelete.clear();
			currentSize--;
			return val;
		}

		int i = 0;
		node = head; //already checked the head
		while(node.next != null){ //else, search for the node in the list
			if(i+1 == index){
				toDelete = node.next; //store data
				val = node.next.value;

				node.next = node.next.next; //delete element
				toDelete.clear();
				currentSize--;
				return val;
			}
			node = node.next;
			i++;
		}
		return null;
	}

	@Override
	public int firstIndex(E e) {
		/* TODO ADD CODE HERE */
		int index = 0;
		for(Node<E> node = head; node != null; node = node.next){
			if(node.value.equals(e)){
				return index;
			}
			index++;
		}
		return -1; //elem not found
	}

	@Override
	public E get(int index) {
		/* TODO ADD CODE HERE */
		if(index < 0 || index >= currentSize)
			throw new IndexOutOfBoundsException("Can't access element at index: " + index);

		int i = 0;
		for(Node<E> node = head; node != null; node = node.next){
			if(i == index){
				return node.value;
			}
			i++;
		}
		return null;
	}



	@SuppressWarnings("unchecked")
	@Override
	public E[] toArray() {
		int index = 0;
		E[] theArray = (E[]) new Comparable[size()]; // Cannot use Object here
		for(Node<E> curNode = this.head; index < size() && curNode  != null; curNode = curNode.getNext(), index++) {
			theArray[index] = curNode.getValue();
		}
		return theArray;
	}

}
