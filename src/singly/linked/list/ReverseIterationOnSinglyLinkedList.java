package singly.linked.list;

public class ReverseIterationOnSinglyLinkedList {

	private Node start = new Node();
	private int listSize = 0;
	
	class Node{
		Integer value;
		Node next;
	}
	
	public void add(int value){
		boolean isValueAdded =false;
		Node newNode = new Node();
		if(start.value == null){
			start.value = value;
			start.next = null;
			listSize =1;
		}else{
			newNode.value = value;
			newNode.next = null;
			Node temp = start;
			while(temp.next != null){
				temp = temp.next;
				listSize++;
			}
			temp.next = newNode;
			
		}
		
		
	}
	
	public int size(){			
		return listSize;
	}
	public void reverseList(){
		Node current = start;
		Node previous = null;
		Node next = null;
		
		while(current != null){
			next = current.next;
			current.next = previous;
			previous = current;
			current = next;
		}
		start = previous;
	}
	
	public void iterate(){
		Node iterate = start;
		while(iterate != null){
			System.out.println(iterate.value);
			iterate = iterate.next;
		}
	}
	
	public static void main(String[] args) {
		ReverseIterationOnSinglyLinkedList list = new ReverseIterationOnSinglyLinkedList();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
//		System.out.println(list.listSize);
		list.reverseList();
		list.iterate();
	}
}
