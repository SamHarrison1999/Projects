# Import statements
from elasticsearch import Elasticsearch
import csv
import json
import pandas
import json_lines


"""

Resources Used:

How To Map An Elasticsearch Index Using The Python Client
https://kb.objectrocket.com/elasticsearch/how-to-map-an-elasticsearch-index-using-the-python-client-266

Pattern Tokenizer
https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-pattern-tokenizer.html

Stop Token Filter
https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-stop-tokenfilter.html

Mixing exact search with stemming
https://www.elastic.co/guide/en/elasticsearch/reference/master/mixing-exact-search-with-stemming.html#mixing-exact-search-with-stemming

Multi-fields
https://www.elastic.co/guide/en/elasticsearch/reference/master/multi-fields.html

Similarity
https://www.elastic.co/guide/en/elasticsearch/reference/current/index-modules-similarity.html

Sort search results
https://www.elastic.co/guide/en/elasticsearch/reference/current/sort-search-results.html

KStem token filter
https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-kstem-tokenfilter.html

"""

def connect():
    return Elasticsearch([{'host': 'localhost'}]) # Initalise Elasticsearch on local host

def mapping():
    # Delete index if already exists
    es.indices.delete(index='test-index', ignore=[400, 404])

    """

    Mappings & settings for index
    Tfidf used for scoring
    Removed stop words
    Tokenized data
    Normalized data
    Stemmed data
    
    """
    
    mapping = {
        "settings": {
            "index": {
              "number_of_shards": "1",
              "similarity": {
                "scripted_tfidf": {
                  "type": "scripted",
                    "script": {
                      "source": "double tf = Math.sqrt(doc.freq); double idf = Math.log((field.docCount+1.0)/(term.docFreq+1.0)) + 1.0; double norm = 1/Math.sqrt(doc.length); return query.boost * tf * idf * norm;"
                    }
                 }
                },
              "analysis": {
                "analyzer": {
                  "english_exact": {
                    "tokenizer": "pattern",
                    "filter": [
                      "stop",
                      "lowercase",
                      "kstem"
                      ]
                    }
                  },
                "tokenizer": {
                  "my_tokenizer": {
                    "type": "pattern",
                    "pattern": "r'\b[^\W]+\b'"
                    }
                  }
              },
              "number_of_replicas": "1",
            }
          },
        "mappings": {
            "properties": {
                "Release Year": {
                    "type": "integer",
                },
                "Title": {
                    "type": "text",
                    "analyzer": "english",
                    "fields": {
                        "raw": {
                            "type": "keyword"
                            },
                        "exact": {
                            "type": "text",
                            "analyzer": "english_exact"
                        }
                    },
                    "similarity": "scripted_tfidf"
                },
                "Origin/Ethnicity": {
                    "type": "text",
                    "similarity": "scripted_tfidf"
                },
                "Director": {
                    "type": "text",
                    "similarity": "scripted_tfidf"
                },
                "Cast": {
                    "type": "text",
                    "similarity": "scripted_tfidf"
                },
                "Genre": {
                    "type": "text",
                    "similarity": "scripted_tfidf"
                },
                "Wiki Page": {
                    "type": "text",
                    "similarity": "scripted_tfidf"
                },
                "Plot": {
                    "type": "text",
                    "similarity": "scripted_tfidf",
                    "analyzer": "english",
                    "fields": {
                        "raw": {
                            "type": "keyword",
                            "similarity": "scripted_tfidf"
                            },
                        "exact": {
                            "type": "text",
                            "similarity": "scripted_tfidf",
                            "analyzer": "english_exact"
                        }
                    }
                }
            }
        }
    }

    # Created index with mappings and settings
    response = es.indices.create(
        index="test-index",
        body=mapping,
        ignore=400 # ignore 400 already exists code
    )

    # Confirmation for index creation
    if 'acknowledged' in response:
        if response['acknowledged'] == True:
            print ("Created Index:", response['index'])

    # Catch API error response
    elif 'error' in response:
        print ("ERROR:", response['error']['root_cause'])
        print ("TYPE:", response['error']['type'])

# Converts CSV file to a json file
def CSV2JSON():
    with open('wiki_movie_plots_deduped.json', 'w') as file:
        file.write(dataset.to_json(orient='records', lines=True))
        file.close()

# Upload first 1000 documents from dataset to elasticsearch
def indexing():
    with open('wiki_movie_plots_deduped.json', 'rb') as file:
        i=1
        for item in json_lines.reader(file):
            item = json.dumps(item)
            decoded = json.loads(item)
            # Adding to the index
            es.index(index='test-index',id = i, body = decoded)
            es.indices.get_mapping(index='test-index')  # Order by elasticsearch build in tf.idf
            i+=1
    file.close()

# Output search results
def summary(res):
    for doc in res['hits']['hits']:
        print("Index: ", doc['_id'])
        print("Release Year: ", doc['_source']['Release Year'])
        print("Title: ", doc['_source']['Title'])
        print("Origin/Ethnicity: ", doc['_source']['Origin/Ethnicity'])
        print("Director: ", doc['_source']['Director'])
        print("Cast: ", doc['_source']['Cast'])
        print("Genre: ", doc['_source']['Genre'])
        print("Wiki Page: ", doc['_source']['Wiki Page'])
        print("Plot: ", doc['_source']['Plot'])
        print("Similarity Score: ", doc['_score']) 

# Searches dataset
def search(ch):
    if ch=='1':
        print ("You have chosen to search for all films released within a particular time period")
        start=input('Enter the min Release Year for the range:')
        end=input('Enter the max Release Year for the range:')
        res = es.search(index="test-index", body={"explain": "true", "query":{"bool":{"should":[{"range":{"Release Year":{"gte": start,"lte": end,"format": "yyyy"}}}]}}},size=1000)
        top10 = es.search(index="test-index", body={"explain": "true", "sort": [ { "_score": { "order": "desc" } } ], "query":{"bool":{"should":[{"range":{"Release Year":{"gte": start,"lte": end,"format": "yyyy"}}}]}}},size=10)
    if ch=='2':
        Title=input('Enter the Title:')
        res=es.search(index='test-index', body={"explain": "true", "query": { "simple_query_string": { "fields": [ "Title" ], "query": Title } } },size=1000)
        top10=es.search(index='test-index', body={"explain": "true", "sort": [ { "_score": { "order": "desc" } } ], "query": { "simple_query_string": { "fields": [ "Title" ], "query": Title } } },size=10)
    if ch=='3':
        OriginEthnicity=input('Enter the Origin/Ethnicity:')
        res=es.search(index='test-index', body={"explain": "true", "query":{"match_phrase":{"Origin/Ethnicity":OriginEthnicity}}},size=1000)
        top10=es.search(index='test-index', body={"explain": "true", "sort": [ { "_score": { "order": "desc" } } ], "query":{"match_phrase":{"Origin/Ethnicity":OriginEthnicity}}},size=10)
    if ch=='4':
        Director=input('Enter the Director:')
        res=es.search(index='test-index', body={"explain": "true", "query": { "simple_query_string": { "fields": [ "Director" ], "query": Director } } },size=1000)
        top10=es.search(index='test-index', body={"explain": "true", "sort": [ { "_score": { "order": "desc" } } ], "query": { "simple_query_string": { "fields": [ "Director" ], "query": Director } } },size=10)
    if ch=='5':
        Cast=input('Enter the Cast:')
        res=es.search(index='test-index', body={"explain": "true", "query": { "simple_query_string": { "fields": [ "Cast" ], "query": Cast } } },size=1000)
        top10=es.search(index='test-index', body={"explain": "true", "sort": [ { "_score": { "order": "desc" } } ], "query": { "simple_query_string": { "fields": [ "Cast" ], "query": Cast } } },size=10)
    if ch=='6':
        Genre=input('Enter the Genre:')
        res=es.search(index='test-index', body={"explain": "true", "query":{"match_phrase":{"Genre":Genre}}},size=1000)
        top10=es.search(index='test-index', body={"explain": "true", "sort": [ { "_score": { "order": "desc" } } ], "query":{"match_phrase":{"Genre":Genre}}},size=10)
    if ch=='7':
        WikiPage=input('Enter the Wiki Page:')
        res=es.search(index='test-index', body={"explain": "true", "query":{"match_phrase":{"Wiki Page":WikiPage}}},size=1000)
        top10=es.search(index='test-index', body={"explain": "true", "sort": [ { "_score": { "order": "desc" } } ], "query":{"match_phrase":{"Wiki Page":WikiPage}}},size=10)
    if ch=='8':
        Plot=input('Enter the Plot:')
        res=es.search(index='test-index', body={"explain": "true", "query": { "simple_query_string": { "fields": [ "Plot" ], "query": Plot } } },size=1000)
        top10=es.search(index='test-index', body={"explain": "true", "sort": [ { "_score": { "order": "desc" } } ], "query": { "simple_query_string": { "fields": [ "Plot" ], "query": Plot } } },size=10)
    return res, top10

if (__name__ == '__main__'):
    es = connect()
    mapping()
    # Reads first 1000 documents from dataset
    dataset = pandas.read_csv('wiki_movie_plots_deduped.csv', sep=',')  
    dataset = dataset.truncate(before=0, after=999)
    print("Read CSV file")
    CSV2JSON()
    indexing()
    print("Indexed first 1000 documents and uploaded them to ElasticSearch")
    print("Removed stopwords from dataset")
    print("Applied Stemming, Normalization & Tokenization to dataset")
    docs=[]
    ch=input('Would you like to search the data set? press y/n:')
    while( ch == "y" or ch == "Y"):
        print("Enter 1 to search for films relased within a particular time frame")
        print("Enter 2 to search for films based their title")
        print("Enter 3 to search for all films based on there origin")
        print("Enter 4 to search for all films directed by a particular director")
        print("Enter 5 to search for films based on their cast")
        print("Enter 6 to search for all films of a particular genre")
        print("Enter 7 to sesrch for films using there Wiki link")
        print("Enter 8 to search for films based their plot")
        ch=input('Enter a choice of query:')
        ch=input('Enter a choice of query:')
        res,top10=search(ch)
        summary(res)

        print("Top 10 results based on similarity score")
        summary(top10)
        ch=input('Would you like to search the data set? press y/n:')
