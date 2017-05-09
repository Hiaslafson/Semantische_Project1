PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX : <http://example.org/> 

INSERT DATA { 
	:jane  a :Person; 
		:gender "female"@en; :age 22;
		:friend :mary, :bob, :bill;
		:loves :bill.
	:mary  a :Person; 
		:gender "female"; :age 22;
		:friend :bob;
		:loves :bill.
	:bob  a :Person;  
		:age 26; 
		:loves :jane.
};

INSERT DATA{ 
	:mary :age 24.
	:bob a :Person; 
	     :age 28.
	:bill  a :Person; 
		:gender "male";
		:friend :mary, :jane. 
}