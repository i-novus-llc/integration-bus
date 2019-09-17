package ru.i_novus.is.example;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/example")
public class RestService {

    @GetMapping(path = "/get/{foo}")
    public HttpStatus getExample(@PathVariable("foo") String foo, @RequestParam Map<String, String> requestParams) {
        if (foo.equals("foo") && requestParams.get("bar").equals("bar")) {
            return HttpStatus.ACCEPTED;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @PostMapping("/post")
    public ResponseModel postExample(@RequestBody RequestModel request) {

        return new ResponseModel(request.getMessage());
    }
}
