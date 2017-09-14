import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import static java.lang.Math.sqrt;
import static java.lang.Math.pow;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Comparison{
	
    private BufferedImage bufferedImage1;//Буфферизация первого изображения
    private BufferedImage bufferedImage2;//Буфферизация второго изображения
    private ArrayList<Pixel> pixels = null;//Массив различающихся пикселей
    private HashMap<Integer,ArrayList<Pixel>> clasters;//Кластеры отличающихся пикселей
    private int distance = 10;// Дистанция мужде отличающимися пикселями для создания кластеров
    
    public Comparison(String image1,String image2){
        try{
            this.bufferedImage1 = ImageIO.read(new File(image1));
            this.bufferedImage2 = ImageIO.read(new File(image2));
            pixels = new ArrayList<>();
            clasters = new HashMap<>();
        }catch(IOException e){
            System.out.println("Image not found");
        }
    }

    public void searchDifferent() throws IOException{//Сравнение		
        if(compareSizes()){////Сравнение размера изображений
            searchDifferentPixels();// Поиск разных пикселей
            dividedIntoClasters();// Определение принадлежности к кластеру
            choseClasters();//РАспределение пикселей по кластерам
            drowRectangle();//Выделение отличий на новом рисунке
        }
        else
                    System.out.println("Images have different sizes");
    }
    
    private void searchDifferentPixels(){// Поиск разных пикселей
        for(int x = 0; x < bufferedImage1.getWidth(); x++){
                for(int y = 0; y < bufferedImage1.getHeight(); y++){
                    if( different(bufferedImage1.getRGB(x,y), bufferedImage2.getRGB(x,y)) ){
                            pixels.add(new Pixel(x,y));
                    }
                }
            }
    }

    private boolean compareSizes(){ //Сравнение размера изображений
        return !(bufferedImage1.getWidth() != bufferedImage2.getWidth() || bufferedImage1.getHeight() != bufferedImage2.getHeight());
    }

    private boolean different(int rgb1, int rgb2){ //Отличие пикселей на 10%
        return colorIndex(rgb1)/colorIndex(rgb2) < 0.9 || colorIndex(rgb1)/colorIndex(rgb2) > 1.1;		
    }

    private double colorIndex(int rgb){//Определение индекса цвета пикселя для сравнения
            int red = (rgb >> 16) & 0xff;
            int green = (rgb >>  8) & 0xff;
            int blue = (rgb) & 0xff;
            double colorIndex = ( sqrt(pow(red,2) + pow(green,2)) + sqrt(pow(green,2) + pow(blue,2)) + sqrt(pow(blue,2) + pow(red,2)))/3;
            return colorIndex;
    }

    private void dividedIntoClasters(){// Определение принадлежности к кластеру
        for(int i = 0; i < pixels.size(); i++){
            for(int j = 0; j < pixels.size(); j++){
                if(!pixels.get(i).getChek()){
                    pixels.get(i).setChek(true);
                    pixels.get(i).setClaster(i);
                    clasters.put(i, new ArrayList<>());//Создание массивов для кластеров
                    
                }
                if(distance(pixels.get(i), pixels.get(j))<distance){
                    pixels.get(j).setClaster(pixels.get(i).getClaster());
                    pixels.get(j).setChek(true);
                }

            }
        }
    }
    
    private double distance(Pixel p1, Pixel p2){//Определение дистанции между точками
        double d = Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2);
        return Math.sqrt(d);
    }
    
    private void drowRectangle() throws IOException{//Выделение различий на 3й картинке
        Graphics2D graph = bufferedImage1.createGraphics();
        graph.setColor(Color.red);
        for(Rectangle r: getRectangles()){
            graph.drawRect(r.getX(), r.getY(), r.getWidth(), r.getHeigth());
        }
        ImageIO.write(bufferedImage1, "png", new File("D:\\image\\comparisonImage.png"));
        
    }
    
    private void choseClasters(){//Распределение пикселей по кластерам
         for(Entry entry: clasters.entrySet()){
            for(Pixel p: pixels){
                if(p.getClaster()==(Integer)entry.getKey()){
                    clasters.get(entry.getKey()).add(p);
                }
            }
        }
        
    }
    
    private ArrayList<Rectangle> getRectangles(){//Создание массива с прямоугольниками для выделения различий
        ArrayList<Rectangle> rectangles = new ArrayList<>();

        for(Entry entry: clasters.entrySet()){
            Rectangle rectangle = new Rectangle();
            int min_x = bufferedImage1.getWidth();
            int min_y = bufferedImage1.getHeight();
            int max_x = 0;
            int max_y = 0;
            for(Pixel p: clasters.get(entry.getKey())){
                if(min_x>p.getX())
                    min_x = p.getX();
                if(max_x<p.getX())
                    max_x = p.getX();
                if(min_y>p.getY())
                    min_y = p.getY();
                if(max_y<p.getY())
                    max_y = p.getY();
            }
            rectangle.setX(min_x-distance);
            rectangle.setY(min_y-distance);
            rectangle.setWidth(max_x-rectangle.getX()+distance);
            rectangle.setHeigth(max_y-rectangle.getY()+ distance);
            rectangles.add(rectangle);
        }
        
        return rectangles;
    }

    
}