package com.example.plugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class ExportAIAssistantPromptAction : AnAction("Export AI Assistant Prompt", "Export AI Assistant prompt file to a specified location", null) {

    override fun actionPerformed(e: AnActionEvent) {
        val basePath = System.getProperty("idea.config.path") // Dynamically get the config path

        if (basePath == null) {
            Messages.showErrorDialog("Could not determine the IntelliJ configuration path.", "Error")
            return
        }

        val ideVersionFolder = ApplicationInfo.getInstance().build.productCode + ApplicationInfo.getInstance().fullVersion
        val targetFilePath = "$basePath/options/AIAssistantPromptLibraryStorage.xml"
        val targetFile = File(targetFilePath)

        if (!targetFile.exists()) {
            Messages.showErrorDialog("File not found: $targetFilePath", "Error")
            return
        }

        val fileChooserDescriptor = FileChooserDescriptor(false, true, false, false, false, false)
            .withTitle("Select Export Folder")
        val exportLocation: VirtualFile? = FileChooser.chooseFile(fileChooserDescriptor, e.project, null)

        if (exportLocation == null) {
            Messages.showInfoMessage("Export operation was canceled.", "Info")
            return
        }

        try {
            val destinationFile = File(exportLocation.path, targetFile.name)
            Files.copy(targetFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
            Messages.showInfoMessage("File exported successfully to ${destinationFile.path}", "Success")
        } catch (ex: Exception) {
            Messages.showErrorDialog("Failed to export file: ${ex.message}", "Error")
        }
    }
}