import java.io.*;

/**
 * Класс для деления файла на части и сборки
 */

public class SplitFiles
{
   /**
    * Функция, соединяющая части (пакеты)
    * @param FilePath - абсолютный адрес местонахождения всех частей файла
    * @return возвращает true в случае успеха, иначе false
    */
   public static boolean join(String FilePath) {
      int count = 0, data;
      try {
         File filename = new File(FilePath);
         OutputStream outfile = new BufferedOutputStream(new FileOutputStream(filename));
         while (true) {
            filename = new File(FilePath + "." + count + ".sp");
            if (filename.exists()) {
               InputStream infile = new BufferedInputStream(new FileInputStream(filename));
               data = infile.read();
               while (data != -1) {
                  outfile.write(data);
                  data = infile.read();
               }
               infile.close();
               count++;
            } else {
               break;
            }
         }
         outfile.close();
      } catch (Exception e) {
         return false;
      } finally {
         for(int i=0; i< count; i++) {
            new File(FilePath + "." + i + ".sp").delete();
         }
      }
      return true;
   }

   /**
    * Функция, делящая файл на части (пакеты)
    * @param FilePath - абсолютный адрес местонахождения файла
    * @param fileIsSplited - если файл подготовлен, его можно начинать отправлять
    * @return возвращает true в случае успеха, иначе false
    */
   public static boolean split(String FilePath, boolean[] fileIsSplited) {
      long Length = 0, splitLength;
      int count = 0, data;
      try {
         File filename = new File(FilePath); //путь файла
         if(!filename.exists()) throw new FileNotFoundException(); //проверка на
         splitLength = filename.length()/6 + 1;
         InputStream infile = new BufferedInputStream(new FileInputStream(filename));
         data = infile.read(); //читаем файл
         while (data != -1) {
            filename = new File(FilePath + "." + count + ".sp"); //создаем файл
            OutputStream outfile = new BufferedOutputStream(new FileOutputStream(filename));
            while (data != -1 && Length < splitLength) {
               outfile.write(data);
               Length++;
               data = infile.read();
            }
            Length = 0;
            outfile.close();
            fileIsSplited[count] = true;
            System.out.println("File: " + filename + " is ready");
            count++;
         }
      } catch (Exception e) {
         e.printStackTrace();
         return false;
      }
      return true;
   }
}