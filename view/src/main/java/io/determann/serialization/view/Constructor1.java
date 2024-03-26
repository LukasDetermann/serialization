package io.determann.serialization.view;

@FunctionalInterface
public non-sealed interface Constructor1<TYPE, P0> extends Constructor<TYPE>
{
   TYPE create(P0 p0);
}
