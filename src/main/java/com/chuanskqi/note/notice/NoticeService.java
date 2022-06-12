package com.chuanskqi.note.notice;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

public class NoticeService {

    public static void notice(String content, Project project) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("Custom Notification Group")
            .createNotification(content, NotificationType.INFORMATION)
            .notify(project);
    }
}
