PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX : <http://example.org/> 

DELETE {  
  GRAPH ?g { :Bill :likes ?o. } } 
INSERT { 
  GRAPH :BGraph {:Bill :likes ?o. } }
WHERE {
  GRAPH ?g { :Bill :likes ?o. }
}