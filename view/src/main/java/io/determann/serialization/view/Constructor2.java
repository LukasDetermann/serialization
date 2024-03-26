package io.determann.serialization.view;

@FunctionalInterface
public non-sealed interface Constructor2<TYPE, P0, P1> extends Constructor<TYPE>
{
   TYPE create(P0 p0, P1 p1);
}
