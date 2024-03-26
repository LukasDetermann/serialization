package io.determann.serialization.view;

import java.util.function.Supplier;

public interface Views
{
   public static <TYPE> PropertyView<TYPE> property(Supplier<TYPE> instanceSupplier)
   {
      return new PropertyView<>(instanceSupplier);
   }

   public static <TYPE> PropertyDeSerializationView<TYPE> propertyDeSerialization(Supplier<TYPE> instanceSupplier)
   {
      return new PropertyDeSerializationView<>(instanceSupplier);
   }

   public static <TYPE> PropertySerializationView<TYPE> propertySerialization(Class<TYPE> type)
   {
      return new PropertySerializationView<>();
   }

   public static <TYPE> Parameter0<TYPE> constructor(Class<TYPE> type)
   {
      return new NParameterImpl<>();
   }
}