//package org.egglog.api.fcm.controller;
//import lombok.RequiredArgsConstructor;
//import org.egglog.api.fcm.model.dto.FcmMessage;
//import org.egglog.api.fcm.model.service.FcmService;
//import org.egglog.utility.utils.MessageUtils;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/v1/message/fcm")
//public class FcmController {
//    private final FcmService fcmService;
//
//    @PostMapping("/topic")
//    public ResponseEntity sendMessageTopic(@RequestBody FcmMessage fcmMessage) {
//        fcmService.sendMessageByTopic(fcmMessage.getTitle(), fcmMessage.getBody(), fcmMessage.getTopicName());
//        return ResponseEntity.ok().body(MessageUtils.success());
//    }
//
//    @PostMapping("/token")
//    public ResponseEntity sendMessageToken(@RequestBody FcmMessage fcmMessage) {
//        fcmService.sendMessageByToken(fcmMessage.getTitle(), fcmMessage.getBody(), fcmMessage.getTargetToken());
//        return ResponseEntity.ok().body(MessageUtils.success());
//    }
//
//    @PostMapping("/all")
//    public ResponseEntity sendMessageAll(@RequestBody FcmMessage fcmMessage) {
//        fcmService.sendMessageAll(fcmMessage.getTitle(), fcmMessage.getBody());
//        return ResponseEntity.ok().body(MessageUtils.success());
//    }
//
//    @PostMapping("/subscribe")
//    public ResponseEntity subscribeByTopic(@RequestBody FcmMessage fcmMessage) {
//        fcmService.subscribeByTopic(fcmMessage.getTargetToken(), fcmMessage.getTopicName());
//        return ResponseEntity.ok().body(MessageUtils.success());
//    }
//}
