PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX : <http://example.org/> 

INSERT DATA { 
  GRAPH :MarysGraph {
    :Mary :likes :Bill.
  }
}
