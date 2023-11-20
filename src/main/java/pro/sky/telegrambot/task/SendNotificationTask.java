package pro.sky.telegrambot.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import pro.sky.telegrambot.service.TelegramSenderService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Component
public class SendNotificationTask {

    private final Logger logger = LoggerFactory.getLogger(SendNotificationTask.class);

    private final NotificationTaskRepository notificationTaskRepository;
    private final TelegramSenderService telegramSenderService;

    public SendNotificationTask(NotificationTaskRepository notificationTaskRepository, TelegramSenderService telegramSenderService) {
        this.notificationTaskRepository = notificationTaskRepository;
        this.telegramSenderService = telegramSenderService;
    }

    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 1 )
    public void sendNotificstiond(){
        LocalDateTime currentDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        logger.info("Task started for dateTime = " + currentDateTime);

        notificationTaskRepository.findAllByDateTime(currentDateTime).forEach(task ->{
            telegramSenderService.send(task.getChatId(), "Напоминание: " + task.getMessage());
            logger.info("Reminder for task with id ={} has been sent", task.getId());
        } );
        logger.info("Task finished for dateTime = " + currentDateTime);
    }

}
