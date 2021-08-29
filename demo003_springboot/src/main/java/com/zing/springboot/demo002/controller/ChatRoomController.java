package com.zing.springboot.demo002.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * create at     2019/9/12 9:31 上午
 *
 * @author zing
 * @version 0.0.1
 */
@Slf4j
@Controller
public class ChatRoomController {
    @PostMapping(value = "test/cut")
    public ResponseEntity<R> TestBodyCut() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(R.success("{\"token\":\"MRiJmBu1FxI9f2Gm/DID7tebwhBiuTOAREhjDQWQVSx+2tOiDfptn5eFWN7sLV0VGtAbqoPXtWMp7vjZsQacZbzS0Eva8oQNCLkmg94Hndyjo/7kZy38VgBYb3ngdEOzjXfr1YHmvofuU7C6+MlYgStYgFUA3FiUHy8oqchy7HUKNbfvv/xinia9Q98qWLE5qDRRm+CiKRyGA22GzkWF63k82YyYKwDrW0IgJBWf2WcZPA==\",\"roleFlag\":\"0\"}"));

    }

}
