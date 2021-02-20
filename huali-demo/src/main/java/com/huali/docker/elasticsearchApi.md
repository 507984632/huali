# elasticsearch API使用

## Rest 风格的url
1. PUT： `put /索引名称/类型名称/文档id`                 创建指定文档id 的文档
2. POST：`post /索引名称/类型名称`                       创建随机文档id 的文档
3. POST：`post /索引名称/类型名称/文档id/_update`        修改文档内容
4. DELETE：`delete /索引名称/类型名称/文档id`            删除文档
5. GET：`get /索引名称/类型名称/文档id`                  查询文档id 为 X的文档
6. POST `post /索引名称/类型名称/_search`                查询所有数据

## 基本类型
1. 字符串类型 \
text(会进行分析解析)、keywork(不会进行分词解析，会将这个当成一个整体)
2. 数值类型 \
long、integer、short、byte、double、float、half float、scaled flloat
3. 日期类型 \
date
4. te布尔值类型 \
boolean
5. 二进制类型 \
binary
6. ...

## 索引的使用
### 1. 创建索引 并设置字段类型

        PUT test2
        {
          "mappings": {             ## 设置映射关系
            "properties": {         ## 设置映射的属性
              "name":{              ## 设置字段名称
                "type": "keyword"   ## 设置字段类型
              },                    
              "age":{               ## 设置字段名称
                "type": "long"      ## 设置字段类型
              },    
              "shengri":{           ## 设置字段名称
                "type": "date"      ## 设置字段类型
              }
            }
          }
        }

### 2. 创建一个文档 

        put /text1/type1/1
        {
            "name":"杨明阳",
            "age":23
        }
        
        结果：
        {
          "_index" : "test1",       ## 索引 默认没有索引 则创建
          "_type" : "type1",        ## 类型 同上
          "_id" : "1",              ## 文档id
          "_version" : 1,           ## 当前文档的版本 修改一次 +1
          "result" : "created",     ## 当前文档的状态 是创建状态
          "_shards" : { 
            "total" : 2,
            "successful" : 1,
            "failed" : 0
          },
          "_seq_no" : 0,
          "_primary_term" : 1
        }

### 3. 获得索引， 类型， 文档的详细信息  
        / 索引 / 类型 / 文档id       ##类型默认就是：_doc \
         7.X 版本 已经不在使用类型 type   \
         8.X 版本 将直接弃用  \
        ## 想要获得那个的详情，就写到那一层 \
        `get 索引`                       ## 获得索引详情 \
        `get 索引 / 类型`                ## 获得类型详情 \
        `get 索引 / 类型 / 文档id`       ## 获得文档详情 \

### 4. 通过 `get _cat` 命令获得 查看其他信息
        get _cat/indices?v          查看当前所有的索引

### 5. 修改文档
#### 5.1 PUT 方式修改 直接就是 添加操作，
        弊端：当本次修改的只有一个字段时，直接提交会将其他的原本不需要修改的字段 `置空`

#### 5.2 POST 方式

        post 索引/_doc/文档id/_update       ## _update 固定写法
        {
            "doc":{                         ## 修改的内容
                "修改的字段":"新的值"       ## 具体要修改那些，可以有多个
            }
        }
        优势： 就是只修改一个字段，其他字段没有，也不会置空
### 6. 删除 索引 类型 文档

        delete 索引 / 类型 / 文档id


​        
​        
​        
## 文档的使用
### 1. 创建文档

        put huali/user/1                            ## 创建了huali索引，user类型，id=1的用户
        {
          "name":"杨明阳",                          ## 名称
          "age":23,                                 ## 年龄
          "desc": "个人简介",                       ## 简介
          "tags":["标签1","标签2","标签3","标签4"]   ## 个人标签
        }

### 2. 查询用户 _search 查询命令

        GET huali/user/_search?q=name:杨          ## 查询当前索引，当前类型下  所有 name 中有 杨 字的用户
        
        注意点：如果 name 字段的类型 是 text 类型，则会将name 字段拆分出来，查询的时候就会按照拆分的查询
        如果是 keyword 的类型， 则会按照整体去查询


​        
### 3. 查询参数 query 查询所用到的参数对象

        GET huali/user/_search
        {
          "query": {                ## 查询参数
            "match": {              ## 单个或多个参数
              "name": "杨"          ## 查询字段 和 参数值
            }
          }
        }

### 4. 只查询 用户的 name 和 age 可以使用 _source属性

        GET huali/user/_search
        {
          "query": {
            "match": {
              "name": "杨"
            }
        
          },
          "_source":["name","age"]          ## 过滤查询出来的属性
        }

### 5. 排序 sort 属性

        GET huali/user/_search
        {
          "query": {
            "match": {
              "name": "杨"
            }
        
          },
          "sort":[                      ## 排序的对象
            {                           ## 对单个字段排序的对象
                "age":{                 ## 字段名称
                    "order":"desc"      ## 排序类型
                }
            }
          ]
        }

### 6. 分页查询 from（起始下标） size(每页条数)

        GET huali/user/_search
        {
          "query": {
            "match": {
              "name": "杨"
            }
        
          },
          "sort":[
            {
              "age":{
                "order":"desc"
              }
            }
          ],
          "from":0, ## limit 中前一个参数
          "size":1  ## limit 中后一个参数
        }   

### 7. 多条件查询  must（and 并且的关联） 查询名字有杨，并且年龄为23 岁的数据

        GET huali/user/_search
        {
          "query": {
            "bool": {               ## boolean 值查询
              "must": [
                {
                  "match": {       ## 查询条件1 
                    "name": "杨"   ## 名称中有杨字
                  }
                },
                
                {
                  "match": {        ## 条件2
                    "age": "23"     ## 年龄为 23
                  }
                }
                
              ]
            }
          }
        }

### 8. 多条件查询 should(or 或者的条件关联)  查询名字有杨 或者 年龄为23 岁的数据

        GET huali/user/_search
        {
          "query": {
            "bool": {
              "should": [
                {
                  "match": {
                    "name": "杨"
                  }
                },
                {
                  "match": {
                    "age": "23"
                  }
                }
              ]
            }
          }
        }

### 9. 多条件查询 must_not （!= 查询出不等于查询条件的数据） 查询所有年龄不为23岁的数据

        GET huali/user/_search
        {
          "query": {
            "bool": {
              "must_not": [
                {
                  "match": {
                    "age": "23"
                  }
                }
              ]
            }
          }
        }

### 10. 多条件查询 (过滤查询 在满足查询条件的情况下，在判断是否满足过滤的条件)    查询名字有杨，且 age >=24 && age <=100

        GET huali/user/_search
        {
          "query": {
            "bool": {
              "must": [
                {
                  "match": {
                    "name": "杨"
                  }
                }
              ],
              "filter": [
                {
                  "range": {
                    "age": {
                      "gte": 24,
                      "lte": 100
                    }
                  }
                }
              ]
            }
          }
        }


### 11. 匹配多个条件 （查询的字段是一个多个值的时候，一个大题中多个子题的，查询的是字题的东西） 多个按照 空格拆分出来

        GET huali/user/_search
        {
          "query": {
            "match": {
              "tags": "1 3 5"       ## 这里 tags 是一个数组的形式，1 3 5 是其中的某些数据中的一部分，这里查询的结果就是 可能包含1 或3 或5 的数据
            }
          }
        }
        
        再举个例子：          这样查询的话可以把 名字中含有 杨 或 峰的数据全部查询出来 其中的关系是 or
        GET huali/user/_search
        {
          "query": {
            "match": {
              "name": "杨 峰"
            }
          }
        }

### 12. 精确查询     【term 查询是直接通过倒排索引指定的词条进行精确的查找】

        term 是倒排索引的形式，所以会精确查找
        match 会使用分词器解析！（线分析文档，然后在通过分析的文档进行查询）

### 13. 多个值的精确查询 

        GET huali/_search
        {
          "query": {
            "bool": {
              "should": [
                {
                  "term": {
                    "t1":"22"
                  }
                },
                 {
                  "term": {
                    "t2":"21"
                  }
                }
              ]
            }
          }
        }

### 14. 高亮显示 highlight 对象

        GET huali/user/_search
        {
          "query": {
            "match": {
              "name": "杨"
            }
          },
          "highlight": {
            "pre_tags": "<p style='color:red'>",        ## 高亮显示的前缀标签
            "post_tags": "</p>",                        ## 高亮显示的后缀标签
            "fields": {                                 ## 高亮显示的字段属性 需要包含所有要高亮部分的字段
              "name":{}                                 ## 高亮显示的那个字段
            }
          }
        }
    
        结果
        
        {
          "took" : 2,
          "timed_out" : false,
          "_shards" : {
            "total" : 1,
            "successful" : 1,
            "skipped" : 0,
            "failed" : 0
          },
          "hits" : {
            "total" : {
              "value" : 1,
              "relation" : "eq"
            },
            "max_score" : 1.0594962,
            "hits" : [
              {
                "_index" : "huali",
                "_type" : "user",
                "_id" : "1",
                "_score" : 1.0594962,
                "_source" : {
                  "name" : "杨明阳",
                  "age" : 23,
                  "desc" : "阳仔"
                },
                "highlight" : {
                  "name" : [
                    "<p style='color:red'>杨</p>明阳"
                  ]
                }
              }
            ]
          }
        }


## 总结
1. _search 是查询命令
2. query 是查询的参数对象
3. _source 是过滤查询字段
4. sort 是排序
5. from size 是分页
6. 多条件查询 bool 中的 must 相当于sql 中的 and 
7. 多条件查询 bool 中的 should 相当于sql 中的 or
8. 多条件查询 bool 中的 must_not 相当于sql 中的 != 
9. 范围查询 filter 
10. 多条件查询 match 多个条件按找 空格 隔开
11. 精确查询(倒排索引) term 
12. 查询高亮 highlight 
