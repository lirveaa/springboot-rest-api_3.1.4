import java.util.*;

import net.REST_API.springbootrestapi.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class SpringRestClient {

    private static final String GET_USERS_URL = "http://91.241.64.178:7081/api/users";
    private static final String CREATE_USERS_URL = "http://91.241.64.178:7081/api/users";
    private static final String UPDATE_USERS_URL = "http://91.241.64.178:7081/api/users";
    private static final String DELETE_USERS_URL = "http://91.241.64.178:7081/api/users/{id}";
    private static final RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    public static void main(String[] args) {
        SpringRestClient springRestClient = new SpringRestClient();
        springRestClient.getUsers();
    }

    private void getUsers(){
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity <String> entity = new HttpEntity<String>("parameters", headers);

        ResponseEntity <String> result = restTemplate.exchange(GET_USERS_URL,HttpMethod.GET, entity,
                String.class);
        List<String> cookie = result.getHeaders().get("Set-Cookie");
        System.out.println(result);

        // Create User
        if(cookie !=null){
            createUser(cookie);
        }
    }

    private void createUser(List<String> cookie){
        headers.set("Cookie", String.join(";", cookie));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        User NewUser  = new User(3L, "James", "Brown", (byte) 50);
        HttpEntity<User> requestBody = new HttpEntity<>(NewUser, headers);
        ResponseEntity<String> resultAddUser = restTemplate.postForEntity(CREATE_USERS_URL, requestBody,
                String.class);
        System.out.println(resultAddUser.getBody());

        //Update User
        if(cookie != null){
            updateUser(cookie);
        }
    }

    private void updateUser(List<String> cookie){
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("id", 3);
        User updateUser = new User(3L, "Tomas", "Shelby",(byte)30);
        HttpEntity<User> updateHttp = new HttpEntity<>(updateUser, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(UPDATE_USERS_URL, HttpMethod.PUT, updateHttp, String.class, params);
        System.out.println(responseEntity.getBody());

        //DELETE USER
        if(cookie != null){
            deleteUser(cookie, updateUser);
        }
    }
    private void deleteUser(List<String> cookie, User updateUser){
        headers.set("Cookie", String.join(";", cookie));
        Map<String, Integer> param = new HashMap<String, Integer>();
        param.put("id",3);
        HttpEntity<User> deleteHttp = new HttpEntity<>(updateUser,headers);
        ResponseEntity<String> deleteUserResult = restTemplate.exchange(DELETE_USERS_URL, HttpMethod.DELETE,
                deleteHttp, String.class, param);
        System.out.println(deleteUserResult.getBody());
    }

}
