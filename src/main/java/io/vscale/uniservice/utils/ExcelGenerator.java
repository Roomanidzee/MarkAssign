package io.vscale.uniservice.utils;

import io.vscale.uniservice.domain.Group;
import io.vscale.uniservice.domain.Cooperator;
import io.vscale.uniservice.domain.Student;
import io.vscale.uniservice.domain.Event;
import io.vscale.uniservice.domain.EventTypeEvaluation;
import io.vscale.uniservice.domain.FileOfService;
import io.vscale.uniservice.repositories.data.CooperatorRepository;
import io.vscale.uniservice.repositories.data.FileOfServiceRepository;
import io.vscale.uniservice.repositories.data.GroupRepository;
import io.vscale.uniservice.services.interfaces.storage.StorageService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.Collections;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * 25.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
@Component
public class ExcelGenerator {

    private final CooperatorRepository cooperatorRepository;
    private final GroupRepository groupRepository;
    private final FileOfServiceRepository fileOfServiceRepository;
    private final StorageService storageService;

    @Value("${storage.path}")
    private String urlPath;

    @Autowired
    public ExcelGenerator(CooperatorRepository cooperatorRepository, GroupRepository groupRepository,
                          FileOfServiceRepository fileOfServiceRepository, StorageService storageService) {
        this.cooperatorRepository = cooperatorRepository;
        this.groupRepository = groupRepository;
        this.fileOfServiceRepository = fileOfServiceRepository;
        this.storageService = storageService;
    }

    public void generateTable(Long cooperatorId, Long groupId){

        Cooperator cooperator = this.cooperatorRepository.findOne(cooperatorId);
        Group group = this.groupRepository.findOne(groupId);

        String applicationType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

        String fileName = group.getTitle() + ".xlsx";
        String fileURL = this.urlPath + fileName;
        File file = new File(fileURL);

        List<String> columns = Arrays.asList("ФИО", "Итоговый балл");
        Set<Student> students = group.getStudents();

        try(Workbook workbook = new HSSFWorkbook();
            FileOutputStream fileOut = new FileOutputStream(file)){

            if(!file.exists()){
                file.createNewFile();
            }

            Sheet sheet = workbook.createSheet("Данные по группе");

            Font commonFont = workbook.createFont();
            commonFont.setBold(false);
            commonFont.setFontHeightInPoints((short) 14);
            commonFont.setColor(IndexedColors.BLACK.getIndex());

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(commonFont);
            cellStyle.setWrapText(true);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);

            Row headerRow = sheet.createRow(0);

            IntStream.range(0, columns.size()).forEach(i ->{

                Cell cell = headerRow.createCell(i);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(columns.get(i));

            });

            AtomicInteger rowNumber = new AtomicInteger(1);

            students.forEach(student -> {

                Row row = sheet.createRow(rowNumber.getAndIncrement());

                String surname = student.getProfile().getSurname();
                String name = student.getProfile().getName();
                String patronymic = student.getProfile().getPatronymic();

                StringBuilder sb = new StringBuilder();
                sb.append(surname).append(" ").append(name).append(" ").append(patronymic);

                Cell cell1 = row.createCell(0);
                cell1.setCellStyle(cellStyle);
                cell1.setCellValue(sb.toString());

                List<Event> events = student.getEvents();

                long finalMark = events.stream()
                                       .mapToLong(event ->
                                                    event.getEventTypeEvaluations()
                                                            .stream()
                                                            .mapToLong(EventTypeEvaluation::getFinalValue)
                                                            .sum()
                                       )
                                       .sum();

                Cell cell2 = row.createCell(1);
                cell2.setCellStyle(cellStyle);
                cell2.setCellValue(finalMark);

            });

            IntStream.range(0, columns.size())
                     .forEach(sheet::autoSizeColumn);

            workbook.write(fileOut);
            this.storageService.uploadFileTos3bucket(fileName, file);

        }catch (IOException e){
            e.printStackTrace();
        }

        FileOfService fileOfService = FileOfService.builder()
                                                   .originalName(fileName)
                                                   .encodedName(" ")
                                                   .type(applicationType)
                                                   .url(fileURL)
                                                   .build();

        this.fileOfServiceRepository.save(fileOfService);

        Set<FileOfService> files = cooperator.getCooperatorFiles();

        if(files == null){
            cooperator.setCooperatorFiles(new HashSet<>(Collections.singleton(fileOfService)));
        }else{
            cooperator.getCooperatorFiles().add(fileOfService);
        }

        this.cooperatorRepository.save(cooperator);

    }

}
