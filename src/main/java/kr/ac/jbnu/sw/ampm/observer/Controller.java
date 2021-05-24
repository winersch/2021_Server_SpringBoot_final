package kr.ac.jbnu.sw.ampm.observer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@RestController
@Slf4j
public class Controller {

    private static HashMap<String, ArrayList<Map<String, Object>>> testDBHashMap = new HashMap<String, ArrayList<Map<String, Object>>>();

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getAllresponseEntity(HttpServletRequest request) {
        ResponseEntity<?> responseEntity = null;

        if(!testDBHashMap.isEmpty()){
            responseEntity = new ResponseEntity<>(testDBHashMap, HttpStatus.OK);
        }else{
            responseEntity = new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getAllResponseEntity(HttpServletRequest request, @PathVariable String id) {
        ResponseEntity<?> responseEntity = null;

        if(!testDBHashMap.isEmpty()){
            if(id != null && !id.equals("") && testDBHashMap.containsKey(id)){
                responseEntity = new ResponseEntity<>(testDBHashMap.get(id), HttpStatus.OK);
            }else {
                responseEntity = new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
            }

        }else{
            responseEntity = new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/post/{id}", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> postResponsEntity(HttpServletRequest request, @PathVariable String id, @RequestBody Map<String, Object> requestMap){
        ResponseEntity<?> responseEntity = null;
        ArrayList<Map<String, Object>> postValueArrayList = null;
        if(id != null && !id.equals("")){

            if(testDBHashMap.containsKey(id)){
                postValueArrayList = testDBHashMap.get(id);
            }else {
                postValueArrayList = new ArrayList<Map<String, Object>>();
            }

            postValueArrayList.add(requestMap);

            if (testDBHashMap.containsKey(id)){
                testDBHashMap.replace(id, postValueArrayList);
            }else{
                testDBHashMap.put(id, postValueArrayList);
            }

            responseEntity = new ResponseEntity<>(requestMap, HttpStatus.OK);

        }else{
            responseEntity = new ResponseEntity<>("NOT_CONTAIN", HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> deleteResponseEntity(HttpServletRequest request, @PathVariable String id){
        ResponseEntity<?> responseEntity = null;

        if(id!=null && !id.equals("")){

            if(testDBHashMap.containsKey(id)){
                testDBHashMap.remove(id);
                responseEntity = new ResponseEntity<>("", HttpStatus.OK);

            } else {
                responseEntity = new ResponseEntity<>("NOT_CONTAIN", HttpStatus.BAD_REQUEST);
            }

        } else {
            responseEntity = new ResponseEntity<>("NOT_CONTAIN", HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    @RequestMapping(value = "/put/{id}", method = RequestMethod.PUT, produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> putResponseEntity(HttpServletRequest request, @PathVariable String id, @RequestBody Map<String, Object> requestMap){
        ResponseEntity<?> responseEntity = null;
        ArrayList<Map<String, Object>> postValueArrayList = null;
        postValueArrayList = testDBHashMap.get(id);

        if(!testDBHashMap.isEmpty()){
            for(Map<String, Object> map : postValueArrayList){
                if(map.keySet().equals(requestMap.keySet())){
                    postValueArrayList.set(postValueArrayList.indexOf(map), requestMap);

                    testDBHashMap.replace(id, postValueArrayList);
                    responseEntity = new ResponseEntity<>(requestMap, HttpStatus.OK);
                }
            }
        }else {
            responseEntity = new ResponseEntity<>("NOT_CONTAIN", HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }


    @RequestMapping(value = "/post/{id}/music", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> postMusicResponseEntity(HttpServletRequest request, @PathVariable String id, @RequestBody Map<String, Object> requestMap){
        ResponseEntity<?> responseEntity = null;
        
        ArrayList<Map<String, Object>> postValueArrayList = null;
        Map<String, Object> musicMap = new HashMap<String, Object>();
        int index = 1;
        if(id != null && !id.equals("")){

            if(testDBHashMap.containsKey(id)){
                postValueArrayList = testDBHashMap.get(id);
                for(Map<String, Object> map : postValueArrayList){
                    if(map.containsKey("music")){
                        musicMap.put("name"+index++, requestMap);
                        postValueArrayList.add(postValueArrayList.indexOf(map), musicMap);
                    } else {
                        musicMap.put("music", requestMap);
                        postValueArrayList.add(musicMap);
                    }
                }

                testDBHashMap.put(id, postValueArrayList);
                responseEntity = new ResponseEntity<>(requestMap, HttpStatus.OK);
            }else {
                postValueArrayList = new ArrayList<Map<String, Object>>();
                responseEntity = new ResponseEntity<>("NOT_CONTAIN", HttpStatus.BAD_REQUEST);
            }

        }else{
            responseEntity = new ResponseEntity<>("NOT_CONTAIN", HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }




}
