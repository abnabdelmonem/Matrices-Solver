package com.example.demo.service;

import java.util.EmptyStackException;

public class NodeStack implements IStack{
    private int size;
    private Node top;
    public NodeStack() {  //constructor
        top=new Node(null,null);
        size=0;
    }
    public int size() {
        return size;
    }
    public boolean isEmpty() {
        if(size==0) {
            return true;
        }return false;
    }
    public void push(Object newElem) {
        Node newTop = new Node(newElem, top);
        top=newTop;
        size++;
    }
    public Object pop() {
        if(isEmpty()) {
            throw new EmptyStackException();
        }size--;
        Node tempNode=top;
        top=top.getPrev();
        tempNode.setPrev(null);
        return tempNode.getElement();
    }
    public Object peek() {
        if(isEmpty()) {
            throw new EmptyStackException();
        }return top.getElement();
    }
}

