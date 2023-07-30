package com.example.tictactoe.api.models

class CustomTree<E> {

    var size = 0
        private set
    var root: Pos<E> = Node<E>(null)
        private set

    fun children(p: Pos<E>): List<Pos<E>> = (p as Node<E>).children.toList()
    fun numChildren(p: Pos<E>): Int = children(p).size

    fun isExternal(p: Pos<E>): Boolean = children(p).isEmpty()
    fun isInternal(p: Pos<E>): Boolean = !isExternal(p)
    fun isRoot(p: Pos<E>): Boolean = p == root

    fun addChild(p: Pos<E>, e: E): Pos<E> {
        val n = Node(e)
        (p as Node<E>).children.add(n)
        return n
    }
    fun setRootChildToRoot(i: Int) {
        root = children(root)[i]
    }
    fun setPos(p: Pos<E>, e: E){
        (p as Node<E>).element = e
    }

    interface Pos<E> {
        val element: E?
    }

    private class Node<E>(
        override var element: E?,
        val children: ArrayList<Pos<E>> = ArrayList<Pos<E>>()
    ): Pos<E>

}