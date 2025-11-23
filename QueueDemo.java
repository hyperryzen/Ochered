import java.util.Arrays;
import java.util.NoSuchElementException;

// 1. Интерфейс очереди
interface Queue<T> {
    void enqueue(T element);
    T dequeue();
    T peek();
    boolean isEmpty();
    int size();
    void clear();
    boolean contains(T element);
}

// 2. Реализация на основе массива (кольцевой буфер)
class ArrayQueue<T> implements Queue<T> {
    private static final int DEFAULT_CAPACITY = 10;
    private static final double GROW_FACTOR = 1.5;
    
    private T[] elements;
    private int front;
    private int rear;
    private int size;
    
    @SuppressWarnings("unchecked")
    public ArrayQueue() {
        this.elements = (T[]) new Object[DEFAULT_CAPACITY];
        this.front = 0;
        this.rear = 0;
        this.size = 0;
    }
    
    @SuppressWarnings("unchecked")
    public ArrayQueue(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        this.elements = (T[]) new Object[initialCapacity];
        this.front = 0;
        this.rear = 0;
        this.size = 0;
    }
    
    @Override
    public void enqueue(T element) {
        if (size == elements.length) {
            resize();
        }
        
        elements[rear] = element;
        rear = (rear + 1) % elements.length;
        size++;
    }
    
    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        
        T element = elements[front];
        elements[front] = null;
        front = (front + 1) % elements.length;
        size--;
        return element;
    }
    
    @Override
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return elements[front];
    }
    
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public void clear() {
        Arrays.fill(elements, null);
        front = 0;
        rear = 0;
        size = 0;
    }
    
    @Override
    public boolean contains(T element) {
        for (int i = 0; i < size; i++) {
            int index = (front + i) % elements.length;
            if (elements[index] != null && elements[index].equals(element)) {
                return true;
            }
        }
        return false;
    }
    
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = (int) (elements.length * GROW_FACTOR);
        T[] newElements = (T[]) new Object[newCapacity];
        
        for (int i = 0; i < size; i++) {
            int index = (front + i) % elements.length;
            newElements[i] = elements[index];
        }
        
        elements = newElements;
        front = 0;
        rear = size;
    }
    
    @Override
    public String toString() {
        if (isEmpty()) {
            return "Queue: []";
        }
        
        StringBuilder sb = new StringBuilder("Queue: [");
        for (int i = 0; i < size; i++) {
            int index = (front + i) % elements.length;
            sb.append(elements[index]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}

// 3. Реализация на основе связного списка
class LinkedQueue<T> implements Queue<T> {
    private static class Node<T> {
        T data;
        Node<T> next;
        
        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }
    
    private Node<T> front;
    private Node<T> rear;
    private int size;
    
    public LinkedQueue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }
    
    @Override
    public void enqueue(T element) {
        Node<T> newNode = new Node<>(element);
        
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }
    
    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        
        T element = front.data;
        front = front.next;
        
        if (front == null) {
            rear = null;
        }
        
        size--;
        return element;
    }
    
    @Override
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return front.data;
    }
    
    @Override
    public boolean isEmpty() {
        return front == null;
    }
    
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public void clear() {
        front = null;
        rear = null;
        size = 0;
    }
    
    @Override
    public boolean contains(T element) {
        Node<T> current = front;
        while (current != null) {
            if (current.data != null && current.data.equals(element)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }
    
    @Override
    public String toString() {
        if (isEmpty()) {
            return "Queue: []";
        }
        
        StringBuilder sb = new StringBuilder("Queue: [");
        Node<T> current = front;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }
}

// 4. Главный класс для демонстрации
public class QueueDemo {
    public static void main(String[] args) {
        System.out.println("=== Демонстрация ArrayQueue ===");
        demoArrayQueue();
        
        System.out.println("\n=== Демонстрация LinkedQueue ===");
        demoLinkedQueue();
        
        System.out.println("\n=== Демонстрация исключений ===");
        demoExceptions();
        
        System.out.println("\n=== Демонстрация метода contains ===");
        demoContains();
    }
    
    private static void demoArrayQueue() {
        Queue<Integer> queue = new ArrayQueue<>();
        
        System.out.println("Добавляем элементы: 10, 20, 30, 40, 50");
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        queue.enqueue(40);
        queue.enqueue(50);
        
        System.out.println("Очередь: " + queue);
        System.out.println("Размер: " + queue.size());
        System.out.println("Первый элемент: " + queue.peek());
        
        System.out.println("\nУдаляем элементы:");
        while (!queue.isEmpty()) {
            System.out.println("Извлечено: " + queue.dequeue());
            System.out.println("Текущая очередь: " + queue);
        }
    }
    
    private static void demoLinkedQueue() {
        Queue<String> queue = new LinkedQueue<>();
        
        System.out.println("Добавляем строки");
        queue.enqueue("First");
        queue.enqueue("Second");
        queue.enqueue("Third");
        queue.enqueue("Fourth");
        
        System.out.println("Очередь: " + queue);
        System.out.println("Размер: " + queue.size());
        System.out.println("Первый элемент: " + queue.peek());
        
        System.out.println("\nУдаляем 2 элемента:");
        System.out.println("Извлечено: " + queue.dequeue());
        System.out.println("Извлечено: " + queue.dequeue());
        System.out.println("Текущая очередь: " + queue);
        
        System.out.println("\nДобавляем новые элементы:");
        queue.enqueue("Fifth");
        queue.enqueue("Sixth");
        System.out.println("Очередь: " + queue);
        
        queue.clear();
        System.out.println("\nПосле очистки:");
        System.out.println("Пуста ли очередь: " + queue.isEmpty());
        System.out.println("Размер: " + queue.size());
    }
    
    private static void demoExceptions() {
        Queue<Integer> queue = new ArrayQueue<>();
        
        try {
            queue.dequeue();
        } catch (NoSuchElementException e) {
            System.out.println("Поймано исключение при dequeue: " + e.getClass().getSimpleName());
        }
        
        try {
            queue.peek();
        } catch (NoSuchElementException e) {
            System.out.println("Поймано исключение при peek: " + e.getClass().getSimpleName());
        }
    }
    
    private static void demoContains() {
        Queue<String> queue = new LinkedQueue<>();
        
        queue.enqueue("Apple");
        queue.enqueue("Banana");
        queue.enqueue("Orange");
        
        System.out.println("Очередь: " + queue);
        System.out.println("Содержит 'Apple': " + queue.contains("Apple"));
        System.out.println("Содержит 'Grape': " + queue.contains("Grape"));
        System.out.println("Содержит 'Banana': " + queue.contains("Banana"));
        
        queue.dequeue();
        System.out.println("\nПосле удаления первого элемента:");
        System.out.println("Очередь: " + queue);
        System.out.println("Содержит 'Apple': " + queue.contains("Apple"));
    }
}