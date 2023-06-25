  Set shouldnt be ordered because of this after user remove some elements of set, new additions will take the place of old ones,
this way it wont be ordered. (It is my solution)

  For test code, after starting it and testing some collection yourself, if you exit (by entering 0) there will be exception test,
iterator remove test and pool function test (for int and string linked list).

  If you try to removeall or retainall for linked list it will automatically throw exception, because it is fifo. Remove function
will only work if the element will remove is in the start. (for example 1, 2, 3, 4 -> remove(1) this will work, but remove(2) wont work)

  I put get and set for ArrayList, just because i wanted to make difference between arraylist and hashset.
  
  I put clone, toString and equals method for all classes to make them proper classes.
  
  Documentations are in the JavaDocumentations directory.
