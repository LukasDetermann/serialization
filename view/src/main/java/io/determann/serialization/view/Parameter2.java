package io.determann.serialization.view;

public interface Parameter2<TYPE, P1, P2>
{
   ConstructorDeSerializationView<TYPE> constructor(Constructor2<TYPE, P1, P2> constructor);
}
