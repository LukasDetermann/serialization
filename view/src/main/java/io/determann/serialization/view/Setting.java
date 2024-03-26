package io.determann.serialization.view;

public interface Setting
{
   /**
    * like io.determann.xml to avoid name clashes
    */
   String getNamespace();

   String getName();

   Object getValue();
}
