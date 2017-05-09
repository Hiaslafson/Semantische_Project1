PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX : <http://example.org/> 

DELETE {?p :age ?age_old}
INSERT {?p :age ?age_new}
WHERE 
  { ?p a :Person. 
    ?p :age ?age_old.
    BIND(?age_old + 1 AS ?age_new)	
  }
