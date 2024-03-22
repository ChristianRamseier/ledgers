package org.ledgers

import reactor.core.publisher.Flux

class Fluxer {
    companion object {
        fun <A,B,C,V> combineLatest(a: Flux<A>, b: Flux<B>, c: Flux<C>, v: (a: A, b: B, c: C) -> V): Flux<V> {
            return Flux.combineLatest(a,b,c) { array ->
                @Suppress("UNCHECKED_CAST")
                v.invoke(array[0] as A, array[1] as B, array[2] as C)
            }
        }
        fun <A,B,C,D,V> combineLatest(a: Flux<A>, b: Flux<B>, c: Flux<C>, d: Flux<D>, v: (a: A, b: B, c: C, d: D) -> V): Flux<V> {
            return Flux.combineLatest(a,b,c,d) { array ->
                @Suppress("UNCHECKED_CAST")
                v.invoke(array[0] as A, array[1] as B, array[2] as C, array[3] as D)
            }
        }
    }
}
