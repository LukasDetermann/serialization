package io.determann.serialization.view;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class NParameterImpl<TYPE, P1, P2> implements Parameter0<TYPE>, Parameter1<TYPE, P1>, Parameter2<TYPE, P1, P2>
{
   private final List<Parameter<TYPE, ?>> parameters = new ArrayList<>();

   NParameterImpl()
   {

   }

   private NParameterImpl(Parameter<TYPE, P1> parameter)
   {
      parameters.add(requireNonNull(parameter));
   }

   private NParameterImpl(NParameterImpl<TYPE, P1, P2> other, Parameter<TYPE, ?> parameter)
   {
      parameters.addAll(requireNonNull(other.parameters));
      parameters.add(requireNonNull(parameter));
   }

   @Override
   public <P1> Parameter1<TYPE, P1> parameter1(Parameter<TYPE, P1> parameter)
   {
      return new NParameterImpl<>(new ParameterImpl<>(0, parameter));
   }

   @Override
   public <P2> Parameter2<TYPE, P1, P2> parameter2(Parameter<TYPE, P2> parameter)
   {
      //noinspection unchecked
      return (Parameter2<TYPE, P1, P2>) new NParameterImpl<>(this, new ParameterImpl<>(1, parameter));
   }

   @Override
   public ConstructorDeSerializationView<TYPE> constructor(Constructor0<TYPE> constructor)
   {
      return new ConstructorDeSerializationView<>(parameters, constructor);
   }

   @Override
   public ConstructorDeSerializationView<TYPE> constructor(Constructor1<TYPE, P1> constructor)
   {
      return new ConstructorDeSerializationView<>(parameters, constructor);
   }

   @Override
   public ConstructorDeSerializationView<TYPE> constructor(Constructor2<TYPE, P1, P2> constructor)
   {
      return new ConstructorDeSerializationView<>(parameters, constructor);
   }
}
