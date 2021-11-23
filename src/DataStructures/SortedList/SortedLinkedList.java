package SortedList;


/**
 * Implementation of a SortedList using a SinglyLinkedList
 * @author Fernando J. Bermudez &amp; Juan O. Lopez
 * @author Yariel Mercado
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

	/**
	 * Adds an element into the {@code SortedLinkedList}. Three cases must be considered when adding an element,
	 * to preserve ascending order. When there are no elements, the new value becomes the {@code head}. Otherwise, when
	 * the new value is the smallest, it becomes the new {@code head} and the old {@code head} becomes the new {@code head}'s {@code next}
	 * reference. Else, an iteration is performed over the linked list. The new value is inserted either at the end of
	 * the list, or, when the next element is greater than the one to be added. Increases the list's size.
	 *
	 * @param e The element to be added into the {@code SortedList}.
	 */
	@Override
	public void add(E e) {
		/* Special case: Be careful when the new value is the smallest */
		if(currentSize == 0){
			head = new Node<>(e);
		}

		/* When the new value is smaller than head. */
		else if(e.compareTo(head.value) < 0){
			head = new Node<>(e, head); //value added becomes the new head
		}

		/* Iterate over the list until its appropriate position is found. */
		else{
			Node<E> node = head;
			while(node.next != null && e.compareTo(node.next.value) > 0){ //while e is the biggest value
				node = node.next;
			}
			node.next = new Node<>(e, node.next);
		}

		currentSize++;

	}

	/**
	 * Removes an element from the {@code SortedLinkedList}. If the element to remove is at the {@code head}, sets the new {@code head} to
	 * the next element. Else, searches for the element to be removed in the {@code SortedLinkedList}. Decreases the list's size.
	 *
	 * @param e The element to be removed from the list.
	 * @return A boolean indicating whether the element was successfully removed from the {@code SortedLinkedList}.
	 *         Returns false if no such element was found.
	 */
	@Override
	public boolean remove(E e) {
		/* Special case: Be careful when the value is found at the head node */
		Node<E> toDelete;
		if(head.value.equals(e)){
			toDelete = head;
			head = head.next;
			toDelete.clear(); //removes all references stored from the deleted node, saves up space
			currentSize--;
			return true;
		}

		/* Iterates over the linked list looking for the value to be removed. Compares against the next value
		*  to reassign the current node's next value to the node following the one to be deleted. */
		Node<E> node = head;
		while(node.next != null){
			if(node.next.value.equals(e)){ //deletes the element
				toDelete = node.next;
				node.next = node.next.next;
				toDelete.clear();
				currentSize--;
				return true;
			}
			node = node.next;
		}
		return false; //element not found
	}


	/**
	 * Removes the element at the specified index. Throws an {@code IndexOutOfBoundsException} if the index is less than
	 * zero or greater than or equals to the current size. This guarantees that the index to be removed is in the {@code SortedLinkedList}.
	 * Considers two cases. The first occurs when the index to be removed is zero, the {@code head} node. If the index to be
	 * removed isn't the first, iterates over the {@code SortedLinkedList} until the correct index is found. Doesn't call
	 * {@code remove()} in order to reduce time complexity, given that this would result in an additional iteration of the list.
	 * Decreases the list's size.
	 *
	 * @param index The index to be removed in the {@code SortedLinkedList}.
	 * @return The value of the element at the removed index.
	 */
	@Override
	public E removeIndex(int index) {
		/* Special case: Be careful when index = 0 */
		if(index < 0 || index >= currentSize)
			throw new IndexOutOfBoundsException("Can't remove out of bounds index: " + index);

		Node<E> toDelete;
		E val = head.getValue(); //value to be removed, dummy value here
		Node<E> node;

		/* Element to be removed is the head. */
		if(index == 0){
			toDelete = head;
			val = head.value;
			head = head.next;
			toDelete.clear();
			currentSize--;
			return val;
		}

		/* Iterates over the list in search of the index to be removed. */
		int i = 0;
		node = head;
		while(node.next != null){
			if(i+1 == index){ //one step behind in the search
				/* Stores the node to be cleared and its value. */
				toDelete = node.next;
				val = node.next.value;

				/* Deletes the element */
				node.next = node.next.next;
				toDelete.clear();
				currentSize--;
				break; //break out of the loop and return the value
			}
			node = node.next;
			i++;
		}
		return val;
	}

	/**
	 * Iterates over the {@code SortedLinkedList} in search of the first index of a given element.
	 *
	 * @param e The element to search for.
	 * @return The index of the first occurrence of the given element. Returns -1 if the element isn't found.
	 */
	@Override
	public int firstIndex(E e) {
		int index = 0;
		for(Node<E> node = head; node != null; node = node.next){
			if(node.value.equals(e)){
				return index;
			}
			index++;
		}
		return -1; //elem not found
	}

	/**
	 * Given an index, iterates over the {@code SortedLinkedList} and retrieves the value of the element at that index. Throws
	 * an {@code IndexOutOfBoundsException} if the index is less than zero or greater than or equals to the current size.
	 * This guarantees that the index to be retrieved is in the {@code SortedLinkedList}.
	 *
	 * @param index The index to search for.
	 * @return The value of the element at the specified index.
	 */
	@Override
	public E get(int index) {
		if(index < 0 || index >= currentSize)
			throw new IndexOutOfBoundsException("Can't get out of bounds index: " + index);

		int i = 0;
		E val = head.getValue(); //dummy value
		for(Node<E> node = head; node != null; node = node.next){
			if(i == index){
				/* Store and then return the value of the element at the specified index. */
				val = node.value;
				break; //break out of the loop and return the value
			}
			i++;
		}
		return val;
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
