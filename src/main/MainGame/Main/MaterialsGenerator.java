package main.MainGame.Main;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

import java.util.ArrayList;

/**
 * Генератор материалов для колец
 * */
class MaterialsGenerator {

    /**Лист для материалов*/
    ArrayList<PhongMaterial> materials = new ArrayList<>();

    MaterialsGenerator(){

        final PhongMaterial blackMaterial = new PhongMaterial();
        blackMaterial.setDiffuseColor(Color.MAROON);
        blackMaterial.setSpecularColor(Color.MAROON);
        materials.add(blackMaterial);

        final PhongMaterial orangeMaterial = new PhongMaterial();
        orangeMaterial.setDiffuseColor(Color.ORANGE);
        orangeMaterial.setSpecularColor(Color.ORANGE);
        materials.add(orangeMaterial);

        final PhongMaterial yellowMaterial = new PhongMaterial();
        yellowMaterial.setDiffuseColor(Color.YELLOW);
        yellowMaterial.setSpecularColor(Color.YELLOW);
        materials.add(yellowMaterial);

        final PhongMaterial yellowGreenMaterial = new PhongMaterial();
        yellowGreenMaterial.setDiffuseColor(Color.YELLOWGREEN);
        yellowGreenMaterial.setSpecularColor(Color.YELLOWGREEN);
        materials.add(yellowGreenMaterial);

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.GREEN);
        greenMaterial.setSpecularColor(Color.GREEN);
        materials.add(greenMaterial);

        final PhongMaterial lightGreenMaterial = new PhongMaterial();
        lightGreenMaterial.setDiffuseColor(Color.LIGHTGREEN);
        lightGreenMaterial.setSpecularColor(Color.LIGHTGREEN);
        materials.add(lightGreenMaterial);

        final PhongMaterial cyanMaterial = new PhongMaterial();
        cyanMaterial.setDiffuseColor(Color.CYAN);
        cyanMaterial.setSpecularColor(Color.CYAN);
        materials.add(cyanMaterial);

        final PhongMaterial darkCyanMaterial = new PhongMaterial();
        darkCyanMaterial.setDiffuseColor(Color.DARKCYAN);
        darkCyanMaterial.setSpecularColor(Color.DARKCYAN);
        materials.add(darkCyanMaterial);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.BLUE);
        blueMaterial.setSpecularColor(Color.BLUE);
        materials.add(blueMaterial);

        final PhongMaterial darkBlueMaterial = new PhongMaterial();
        darkBlueMaterial.setDiffuseColor(Color.DARKBLUE);
        darkBlueMaterial.setSpecularColor(Color.DARKBLUE);
        materials.add(darkBlueMaterial);

        final PhongMaterial violetMaterial = new PhongMaterial();
        violetMaterial.setDiffuseColor(Color.VIOLET);
        violetMaterial.setSpecularColor(Color.VIOLET);
        materials.add(violetMaterial);

        final PhongMaterial darkMagentaMaterial = new PhongMaterial();
        darkMagentaMaterial.setDiffuseColor(Color.DARKMAGENTA);
        darkMagentaMaterial.setSpecularColor(Color.DARKMAGENTA);
        materials.add(darkMagentaMaterial);

        final PhongMaterial magentaMaterial = new PhongMaterial();
        magentaMaterial.setDiffuseColor(Color.MAGENTA);
        magentaMaterial.setSpecularColor(Color.MAGENTA);
        materials.add(magentaMaterial);

        final PhongMaterial pinkMaterial = new PhongMaterial();
        pinkMaterial.setDiffuseColor(Color.PINK);
        pinkMaterial.setSpecularColor(Color.PINK);
        materials.add(pinkMaterial);

        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.RED);
        redMaterial.setSpecularColor(Color.RED);
        materials.add(redMaterial);
    }

    /**
     * Возвращает элемент по id
     * */
    PhongMaterial GetMAterialById(int id){
        if(id >= materials.size()){
            return materials.get(0);
        }
        return materials.get(id);
    }

    /**
     * Возвращает текстуру для шестов
     * */
    PhongMaterial getFieldTexture(){
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image(getClass().getResourceAsStream("/Resources/wooden-texture.jpg")));
        return material;
    }
}
