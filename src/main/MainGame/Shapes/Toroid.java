/*
 * Copyright (c) 2017 Peter Lager
 * <quark(a)lagers.org.uk> http:www.lagers.org.uk
 * 
 * This software is provided 'as-is', without any express or implied warranty.
 * In no event will the authors be held liable for any damages arising from
 * the use of this software.
 * 
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it freely,
 * subject to the following restrictions:
 * 
 * 1. The origin of this software must not be misrepresented;
 * you must not claim that you wrote the original software.
 * If you use this software in a product, an acknowledgment in the product
 * documentation would be appreciated but is not required.
 * 
 * 2. Altered source versions must be plainly marked as such,
 * and must not be misrepresented as being the original software.
 * 
 * 3. This notice may not be removed or altered from any source distribution.
 */
package main.MainGame.Shapes;

import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

/**
 * Класс тороида
 */
public class Toroid extends MeshView {

    // Default values

    private static int NBR_RING_SEGMENTS = 64;
    private static int NBR_TUBE_SEGMENTS = 32;
    // Stores the number of segments which controls the surface 'smmothness'
    private final int nbrRingSegments;
    private final int nbrTubeSegments;
    private final int nbrRingSteps;
    private final int nbrTubeSteps;
    // The dimensions of the toroid
    protected float ringRad;
    protected float tubeRadX, tubeRadY;
    protected float tubeZeroPos; // radians

    /**
     * Create a Toroid using the default number of segments.
     *
     * @param ringRad the radius of the ring
     * @param tubeRadX the radius of the tube along the X axis
     * @param tubeRadY the radius of the tube along the Y axis
     * @param zeroAngle rotates the zero-position round the tube (degrees)
     */
    public Toroid(float ringRad, float tubeRadX, float tubeRadY, float zeroAngle) {
        this(NBR_RING_SEGMENTS, NBR_TUBE_SEGMENTS, // default 64 x 32
                ringRad, tubeRadX, tubeRadY, zeroAngle
        );
    }

    /**
     * Create a Toroid with a user defined number of segments. Using small
     * numbers can give a very angular toroid.
     *
     * @param nbrRingSegments number of segments round the ring
     * @param nbrTubeSegments number of segments round the tube
     * @param ringRad the radius of the ring
     * @param tubeRadX the radius of the tube along the X axis
     * @param tubeRadY the radius of the tube along the Y axis
     * @param zeroAngle rotates the zero-position round the tube (degrees)
     */
    public Toroid(int nbrRingSegments, int nbrTubeSegments, float ringRad, float tubeRadX, float tubeRadY, float zeroAngle) {
        this.nbrRingSegments = nbrRingSegments;
        this.nbrTubeSegments = nbrTubeSegments;
        this.ringRad = ringRad;
        this.tubeRadX = tubeRadX;
        this.tubeRadY = tubeRadY;
        this.tubeZeroPos = (float) Math.toRadians(zeroAngle);
        nbrRingSteps = nbrRingSegments + 1;
        nbrTubeSteps = nbrTubeSegments + 1;

        setMesh(new TriangleMesh());
        calcPoints();
        calcTexturePoints(1, 1);
        calcFacesPT();
        // Create the default texture
        PhongMaterial material = new PhongMaterial(Color.BURLYWOOD);
        setMaterial(material);
        // Start with filled shape
        setDrawMode(DrawMode.FILL);
        // Makes it easier to see the wireframe
        setCullFace(CullFace.BACK);
    }

    /**
     * Calculate the vertex points based on toroid dimensions and the
     * tube zero start angle.
     */
    private void calcPoints() {
        Point3D[][] coord = new Point3D[nbrRingSteps][nbrTubeSteps];
        // Calculate segment size in radians
        float ringDeltaAng = (float) (2 * Math.PI / nbrRingSegments);
        float tubeDeltaAng = (float) (2 * Math.PI / nbrTubeSegments);

        // Calculate the XY coordinates of the tube in the Z=0 plane
        for (int t = 0; t < nbrTubeSteps; t++) {
            float angle = tubeZeroPos + t * tubeDeltaAng;
            coord[0][t] = new Point3D(
                    ringRad + tubeRadX * Math.cos(angle),
                    tubeRadY * Math.sin(angle),
                    0
            );
        }
        // Calculate all the points for all the other ring segments
        for (int r = 1; r < nbrRingSteps; r++) {
            float angle = r * ringDeltaAng;
            float sinA = (float) Math.sin(angle);
            float cosA = (float) Math.cos(angle);
            for (int t = 0; t < nbrTubeSteps; t++) {
                Point3D point0 = coord[0][t];
                coord[r][t] = new Point3D(
                        point0.getX() * cosA,
                        point0.getY(),
                        point0.getX() * sinA
                );
            }
        }
        // Transfer to float array
        float[] points = new float[nbrRingSteps * nbrTubeSteps * 3];
        int idx = 0;
        for (int t = 0; t < nbrTubeSteps; t++) {
            for (int r = 0; r < nbrRingSteps; r++) {
                points[idx++] = (float) coord[r][t].getX();
                points[idx++] = (float) coord[r][t].getY();
                points[idx++] = (float) coord[r][t].getZ();
            }
        }
        TriangleMesh mesh = (TriangleMesh) getMesh();
        mesh.getPoints().setAll(points);
    }

    /**
     * Calculate the texture coordinates based on number of texture repeats.
     * 
     * @param nbrRingRepeats number of texture repeats round the ring
     * @param nbrTubeRepeats number of texture repeats round the tube
     */
    private void calcTexturePoints(float nbrRingRepeats, float nbrTubeRepeats) {
        float deltaU = nbrRingRepeats / nbrRingSegments;
        float deltaV = nbrTubeRepeats / nbrTubeSegments;
        float[] uv = new float[nbrRingSteps * nbrTubeSteps * 2];
        int idx = 0;
        for (int t = 0; t < nbrTubeSteps; t++) {
            for (int r = 0; r < nbrRingSteps; r++) {
                uv[idx++] = r * deltaU;
                uv[idx++] = t * deltaV;
            }
        }
        TriangleMesh mesh = (TriangleMesh) getMesh();
        mesh.getTexCoords().setAll(uv);
    }

    /**
     * Calculate the triangle data using vertex and texture coordinate array
     * data.
     */
    private void calcFacesPT() {
        int idx = 0;
        int[] faces = new int[nbrRingSegments * nbrTubeSegments * 12];
        for (int t = 0; t < nbrTubeSegments; t++) {
            for (int r = 0; r < nbrRingSegments; r++) {
                int idxTL = r + t * nbrRingSteps;
                int idxBL = r + (t + 1) * nbrRingSteps;
                int idxBR = r + 1 + (t + 1) * nbrRingSteps;
                int idxTR = r + 1 + t * nbrRingSteps;
                // Top left triangle
                faces[idx++] = idxTL;   // Point
                faces[idx++] = idxTL;   // Texture
                faces[idx++] = idxBL;   // Point
                faces[idx++] = idxBL;   // Texture
                faces[idx++] = idxTR;   // Point
                faces[idx++] = idxTR;   // Texture
                // Bottom right triangle
                faces[idx++] = idxBL;   // Point
                faces[idx++] = idxBL;   // Texture
                faces[idx++] = idxBR;   // Point
                faces[idx++] = idxBR;   // Texture
                faces[idx++] = idxTR;   // Point
                faces[idx++] = idxTR;   // Texture
            }
        }
        TriangleMesh mesh = (TriangleMesh) getMesh();
        mesh.getFaces().setAll(faces);
    }

}
