import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Yang_my
 * @sine 2021/2/23
 */
@SpringBootTest
public class EsAPI {


    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    /******************************                         关于索引的方法                               ******************************************/

    /**
     * 创建索引
     */
    @Test
    public void createIdex() throws IOException {
        // 创建索引的请求
        CreateIndexRequest request = new CreateIndexRequest("test_import");
        // 执行创建索引的请求，并接受响应的结果
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        // 打印查看 响应结果
        System.out.println(response);

    }

    /**
     * 判断索引存不存在
     */
    @Test
    void existindex() throws IOException {
        // 创建 判断存不存在索引的请求
        GetIndexRequest request = new GetIndexRequest("huali");
        // 执行判断是否存在  并将结果返回
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.err.println(exists);
    }

    /**
     * 获得索引
     */
    @Test
    void getIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("huali");
        GetIndexResponse response = client.indices().get(request, RequestOptions.DEFAULT);
        // 获得的索引对象
        System.out.println(response);
    }

    /**
     * 删除索引
     */

    @Test
    void deleteIndex() throws IOException {
        // 创建删除请求
        DeleteIndexRequest request = new DeleteIndexRequest("huali");
        // 执行删除请求 并接受删除索引的结果
        AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
        // 通过结果判断 是否执行删除成功
        boolean acknowledged = response.isAcknowledged();
        System.out.println(acknowledged);
    }


    /******************************                         关于文档的方法                               ******************************************/


    /**
     * 添加文档 (用户文档)
     */
    @Test
    void addDocument() throws IOException {
        // 创建对象
        User user = new User("杨明阳", 32);
        // 创建添加文档的请求
        IndexRequest request = new IndexRequest("huali");

        // 规则 put /索引/_doc/文档id
        request.id("1");
        // 设置超时时间为 1秒 seconds 秒
        request.timeout(TimeValue.timeValueSeconds(1));
//        request.timeout("1s");

        // 将用户对象转换成 json 格式放入请求中 ,且表明是json 数据
        request.source(JSONObject.toJSONString(user), XContentType.JSON);

        // 执行并接受创建文档的 结果
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);

        // 查看当前文档的状态
        System.out.println(response.status());
    }

    /**
     * 判断文档是否存在
     */
    @Test
    void existDocument() throws IOException {
        // 创建 获得文档的请求
        GetRequest getRequest = new GetRequest("huali", "1");
        // 因为这里是判断存不存在这条数据  所以不获得 _source 的上下文了
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");

        // 执行判断 文档是否存在的请求
        boolean exists = client.exists(getRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    /**
     * 获得文档信息
     */
    @Test
    void getDocument() throws IOException {
        // 创建 获得文档的请求
        GetRequest getRequest = new GetRequest("huali", "1");
        // 执行获得文档的请求，并将文档数据返回
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);

        // 拿到用户的 JSON 数据
        String userData = getResponse.getSourceAsString();
        System.out.println(userData);

        // 查看整个 响应对象 这个就是通过 命令行 直接获得的文档的 JSON格式数据，有所有的部分
        System.out.println(getResponse);
    }

    /**
     * 修改文档
     */
    @Test
    void updateDocument() throws IOException {
        // 创建 修改文档的请求对象
        UpdateRequest updateRequest = new UpdateRequest("huali", "1");
        // 设置超时时间
        updateRequest.timeout(TimeValue.timeValueSeconds(1));

        //创建修改对象
        User user = new User("删除该对象吧", 32);
        // 将修改的对象 放入修改请求中
        updateRequest.doc(JSONObject.toJSONString(user), XContentType.JSON);

        // 执行修改的请求
        UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);

        // 通过响应结果查看本次修改是否成功
        RestStatus status = updateResponse.status();
        // 该响应状态为 枚举值
        System.out.println(status);
    }

    /**
     * 删除文档
     */
    @Test
    void deleteDocument() throws IOException {
        // 创建 删除文档的请求对象
        DeleteRequest deleteRequest = new DeleteRequest("huali", "1");
        // 设置超时时间
        deleteRequest.timeout(TimeValue.timeValueSeconds(1));

        // 执行 删除文档的请求 并获得响应对象
        DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        // 查看 删除文档请求的响应状态
        RestStatus status = deleteResponse.status();
        System.out.println(status);
    }


    /*******************************************                       批量对文档的操作                            *****************************/


    /**
     * 批量添加文档
     */
    @Test
    void bulkAddDocument() throws IOException {
        // 创建 批量请求对象
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout(TimeValue.timeValueSeconds(10));

        // 造数据
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            userList.add(new User("老" + i, i));
        }

        // 批处理部分 (也就是便利的操作)
        for (int i = 0; i < userList.size(); i++) {
            bulkRequest.add(
                    // 创建 添加文档的请求
                    new IndexRequest("huali")
                            // 链式编程 赋值id
                            .id("" + (i + 1))
                            // 添加的数据， 数据的类型
                            .source(JSONObject.toJSONString(userList.get(i)), XContentType.JSON));
        }

        // 执行批处理请求，并获得响应结果
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        // 用于判断本次批处理请求是否成功     false 代表成功  true 代码不成功
        boolean fragment = bulkResponse.isFragment();
        System.out.println(fragment);
    }


    /********************************************************                    对文档的查询操作                       ************************************/

    /**
     * 查询文档
     */
    @Test
    void searchDocument() throws IOException {
        // 创建 查询的请求对象
        SearchRequest searchRequest = new SearchRequest("huali");
        // 构建搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 查询条件， 可以通过 QueryBuilders 这个类来快速的获得查询条件
        /**
         * 查询条件可以 通过 QueryBuilders 这个对象来快速实现
         * 1. 精确匹配某个字段 QueryBuilders.termQuery("name", "杨");
         * 2. 全部字段匹配 QueryBuilders.matchAllQuery();
         */
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("age", "32");
        // 将查询条件放入， 如果有多个条件则需要分开都放入  sourceBuilder 中
        sourceBuilder.query(termQueryBuilder);
        // 设置超时时间 60s
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        // 将构建的搜索条件放入 请求对象中
        searchRequest.source(sourceBuilder);

        // 执行 查询请求 并获得查询的结果
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        // 通过返回的响应对象 获得 hits 对象（该对象中存在本次的查询结果）
        SearchHits hits = searchResponse.getHits();
        System.out.println("hits:" + JSONObject.toJSONString(hits));
        System.out.println("=============================");
        // 通过 hits 对象遍历每一个对象
        for (SearchHit hit : hits) {
            // 通过 hit  一个map集合
            System.out.println(hit.getSourceAsMap());
        }

    }

}

@Data
@NoArgsConstructor
@AllArgsConstructor
class User {
    String name;
    Integer age;
}
