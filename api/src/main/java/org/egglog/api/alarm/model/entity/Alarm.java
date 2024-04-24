package org.egglog.api.alarm.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.egglog.api.group.model.entity.Group;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.worktype.model.entity.WorkType;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    private LocalTime alarmTime;

    @Column(name = "alarm_replay_cnt")
    private int replayCnt;

    @Column(name = "alarm_replay_time")
    private int replayTime;

    @Column(name = "is_alarm_on")
    private Boolean isAlarmOn;      //알람 상태 On/Off

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workType_id")
    private WorkType workType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
