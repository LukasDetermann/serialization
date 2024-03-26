package io.determann.serialization.view;

public interface Parameter1<TYPE, P1>
{
   <P2> Parameter2<TYPE, P1, P2> parameter2(Parameter<TYPE, P2> parameter);

   ConstructorDeSerializationView<TYPE> constructor(Constructor1<TYPE, P1> constructor);
}
