package fbo.costa.vagalumelyrics.util

interface Interface_Impl {

    interface ChildView<T> {
        fun getChild(p0: T): Int
    }

    interface NetworkChecker {
        fun checkNetwork(isOnline: Boolean)
    }
}
