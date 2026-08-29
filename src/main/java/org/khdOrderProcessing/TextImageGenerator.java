package org.khdOrderProcessing;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;



public class TextImageGenerator {

    private static final int BOWL_IMAGE_WIDTH = 2700;
    private static final int BOWL_IMAGE_HEIGHT = 2700;
    private static final int PLATE_IMAGE_WIDTH = 3180;
    private static final int PLATE_IMAGE_HEIGHT = 3180;
    private static final int MUG_IMAGE_WIDTH = 2580;
    private static final int MUG_IMAGE_HEIGHT = 1029;
    private static final int UTENSILS_IMAGE_WIDTH = 2495;
    private static final int UTENSILS_IMAGE_HEIGHT = 993;
    private static final int PLACEMAT_IMAGE_WIDTH = 5400;
    private static final int PLACEMAT_IMAGE_HEIGHT = 7350;
    private static final int BOWL_DEFAULT_YOFFSET = 470;
    private static final int PLATE_DEFAULT_YOFFSET = 454;
    private static final int MUG_DEFAULT_YOFFSET = 160;
    private static final int PLACEMAT_DEFAULT_YOFFSET = -2673;
    private static final int MUG_DEFAULT_XOFFSET = -723;
    private static final Font fiddlestixOutline = loadFiddlestixOutline();
    private static final Font fiddlestixSolid = loadFiddlestixSolid();
    private static final int[] offsetsB = setBowlOffsets();
    private static final int[] offsetsP = setPlateOffsets();
    private static final int[] offsetsM = setMugOffsets();
    private static final int[] offsetsU = setUtensilsOffsets();
    private static final int[] offsetsPM = setPlacematOffsets();
    private static final String[] UtensilsColors = setUtensilsColor();
    private static final int TYPE_BOWL = 1;
    private static final int TYPE_PLATE = 2;
    private static final int TYPE_MUG = 3;
    private static final int TYPE_UTENSILS = 4;
    private static final int TYPE_PLACEMAT = 5;
    private static final Color TEXT_COLOR = new Color(0, 0, 0);
    private static final String BACKGROUND_FILE = "background.jpg";



    public static void renderBowl(String name, int bgID)
    {
        try {
            final String outputFile = "/var/www/khdOrders/b"+bgID+"/"+name+".jpg";
            Font textFont = deriveFont(fiddlestixOutline, 180f);
            int yOffset = getYoffset(bgID, TYPE_BOWL);

            ColorSpace sRGB = ColorSpace.getInstance(ColorSpace.CS_sRGB);
            BufferedImage image = createBufferedImage(BOWL_IMAGE_WIDTH, BOWL_IMAGE_HEIGHT);
            Graphics2D g2d = image.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            BufferedImage background = ImageIO.read(
                    TextImageGenerator.class.getResourceAsStream("/bowls/"+bgID+".jpg")
            );
            g2d.drawImage(background, 0, 0, BOWL_IMAGE_WIDTH, BOWL_IMAGE_HEIGHT, null);

            g2d.setFont(textFont);
            g2d.setColor(TEXT_COLOR);

            // Measure text
            FontMetrics metrics = g2d.getFontMetrics(textFont);
            int textWidth = metrics.stringWidth(name);
            int textHeight = metrics.getHeight();
            if (textWidth > 1140) {
                textFont = deriveFont(fiddlestixOutline, 180f*(1130f/textWidth));
                g2d.setFont(textFont);
                metrics = g2d.getFontMetrics(textFont);
                textWidth = metrics.stringWidth(name);
                textHeight = metrics.getHeight();
            }

            // Center positioning
            int x = (int)(BOWL_IMAGE_WIDTH/2) - (int)(textWidth/2) + 20;
            int y = ((BOWL_IMAGE_HEIGHT - textHeight) / 2 + BOWL_DEFAULT_YOFFSET - yOffset);

            // Draw text
            g2d.drawString(name, x, y);
            g2d.dispose();

            saveFile(outputFile, image);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void renderPlate(String name, int bgID)
    {
        try {
            final String outputFile = "/var/www/khdOrders/plt"+bgID+"/"+name+".jpg";
            Font textFont = deriveFont(fiddlestixOutline, 198f);
            int yOffset = getYoffset(bgID, TYPE_PLATE);

            ColorSpace sRGB = ColorSpace.getInstance(ColorSpace.CS_sRGB);
            BufferedImage image = createBufferedImage(PLATE_IMAGE_WIDTH, PLATE_IMAGE_HEIGHT);
            Graphics2D g2d = image.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            BufferedImage background = ImageIO.read(
                    TextImageGenerator.class.getResourceAsStream("/plates/"+bgID+".jpg")
            );
            g2d.drawImage(background, 0, 0, PLATE_IMAGE_WIDTH, PLATE_IMAGE_HEIGHT, null);

            g2d.setFont(textFont);
            g2d.setColor(TEXT_COLOR);

            // Measure text
            FontMetrics metrics = g2d.getFontMetrics(textFont);
            int textWidth = metrics.stringWidth(name);
            int textHeight = metrics.getHeight();
            if (textWidth > 1215) {
                textFont = deriveFont(fiddlestixOutline, 198f*(1215f/textWidth));
                g2d.setFont(textFont);
                metrics = g2d.getFontMetrics(textFont);
                textWidth = metrics.stringWidth(name);
                textHeight = metrics.getHeight();
            }
            // Center positioning
            int x = (int)(PLATE_IMAGE_WIDTH/2) - (int)(textWidth/2) + 10;
            int y = ((PLATE_IMAGE_HEIGHT - textHeight) / 2 + metrics.getAscent()) + PLATE_DEFAULT_YOFFSET - yOffset;

            // Draw text
            g2d.drawString(name, x, y);
            g2d.dispose();

            // Save image
            saveFile(outputFile, image);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void renderMug(String name, int bgID)
    {
        try {
            final String outputFile = "/var/www/khdOrders/mug"+bgID+"/"+name+".jpg";
            Font textFont = deriveFont(fiddlestixOutline, 90f);
            int yOffset = getYoffset(bgID, TYPE_MUG);

            ColorSpace sRGB = ColorSpace.getInstance(ColorSpace.CS_sRGB);
            BufferedImage image = createBufferedImage(MUG_IMAGE_WIDTH, MUG_IMAGE_HEIGHT);
            Graphics2D g2d = image.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            BufferedImage background = ImageIO.read(
                    TextImageGenerator.class.getResourceAsStream("/mugs/"+bgID+".jpg")
            );
            g2d.drawImage(background, 0, 0, MUG_IMAGE_WIDTH, MUG_IMAGE_HEIGHT, null);

            g2d.setFont(textFont);
            g2d.setColor(TEXT_COLOR);

            // Measure text
            FontMetrics metrics = g2d.getFontMetrics(textFont);
            int textWidth = metrics.stringWidth(name);
            int textHeight = metrics.getHeight();
            if (textWidth > 450) {
                textFont = deriveFont(fiddlestixOutline, 90f*(440f/textWidth));
                g2d.setFont(textFont);
                metrics = g2d.getFontMetrics(textFont);
                textWidth = metrics.stringWidth(name);
                textHeight = metrics.getHeight();
            }

            // Center positioning
            int x = (int)(MUG_IMAGE_WIDTH / 2) - (int)(textWidth / 2) + MUG_DEFAULT_XOFFSET;
            int y = ((MUG_IMAGE_HEIGHT - textHeight) / 2 + metrics.getAscent()) + MUG_DEFAULT_YOFFSET - yOffset;

            // Draw text
            g2d.drawString(name, x, y);
            g2d.dispose();

            // Save image
            saveFile(outputFile, image);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void renderUtensils(String name, int bgID)
    {
        try {
            final String outputFile = "/var/www/khdOrders/u"+bgID+"/"+name+".jpg";
            Font textFont = deriveFont(fiddlestixSolid, 90f);
            final Color utensilsColor = getUtensilsColor(bgID);
            int yOffset = getYoffset(bgID, TYPE_UTENSILS);

            ColorSpace sRGB = ColorSpace.getInstance(ColorSpace.CS_sRGB);
            BufferedImage image = createBufferedImage(UTENSILS_IMAGE_WIDTH, UTENSILS_IMAGE_HEIGHT);
            Graphics2D g2d = image.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            BufferedImage background = ImageIO.read(
                    TextImageGenerator.class.getResourceAsStream("/utensils/"+bgID+".jpg")
            );
            g2d.drawImage(background, 0, 0, UTENSILS_IMAGE_WIDTH, UTENSILS_IMAGE_HEIGHT, null);

            g2d.setFont(textFont);
            g2d.setColor(utensilsColor);

            // Measure text
            FontMetrics metrics = g2d.getFontMetrics(textFont);
            int textWidth = metrics.stringWidth(name);
            int textHeight = metrics.getHeight();
            if (textWidth > 490) {
                textFont = deriveFont(fiddlestixSolid, 107f*(480f/textWidth));
                g2d.setFont(textFont);
                metrics = g2d.getFontMetrics(textFont);
                textWidth = metrics.stringWidth(name);
                textHeight = metrics.getHeight();
            }
            // Center positioning

            int centerX = (UTENSILS_IMAGE_WIDTH - textHeight) / 2;
            int centerY = (UTENSILS_IMAGE_HEIGHT + textWidth) / 2;

            if (textHeight != 144) {
                centerX -= (int)(-0.125*(textHeight)+20);
            }

            // Draw text
            AffineTransform originalTransform = g2d.getTransform();

            g2d.rotate(-Math.PI / 2, centerX, centerY);
            g2d.drawString(name, centerX-160, centerY+850);
            g2d.drawString(name, centerX-160, centerY-653);

            g2d.setTransform(originalTransform);

            g2d.dispose();

            // Save image
            saveFile(outputFile, image);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void renderPlacemats(String name, int bgID)
    {
        try {
            final String outputFile = "/var/www/khdOrders/p"+bgID+"/"+name+".jpg";
            Font textFont = deriveFont(fiddlestixOutline, 360f);
            int yOffset = getYoffset(bgID, TYPE_PLACEMAT);

            ColorSpace sRGB = ColorSpace.getInstance(ColorSpace.CS_sRGB);
            BufferedImage image = createBufferedImage(PLACEMAT_IMAGE_WIDTH, PLACEMAT_IMAGE_HEIGHT);
            Graphics2D g2d = image.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            BufferedImage background = ImageIO.read(
                    TextImageGenerator.class.getResourceAsStream("/placemats/"+bgID+".jpg")
            );
            g2d.drawImage(background, 0, 0, PLACEMAT_IMAGE_WIDTH, PLACEMAT_IMAGE_HEIGHT, null);

            g2d.setFont(textFont);
            g2d.setColor(TEXT_COLOR);

            // Measure text
            FontMetrics metrics = g2d.getFontMetrics(textFont);
            int textWidth = metrics.stringWidth(name);
            int textHeight = metrics.getHeight();
            if (textWidth > 2080) {
                textFont = deriveFont(fiddlestixOutline, 360f*(2000f/textWidth));
                g2d.setFont(textFont);
                metrics = g2d.getFontMetrics(textFont);
                textWidth = metrics.stringWidth(name);
                textHeight = metrics.getHeight();
            }

            // Center positioning
            int x = ((PLACEMAT_IMAGE_WIDTH) / 2 ) - (int)(textWidth)/2;
            int y = ((PLACEMAT_IMAGE_HEIGHT - textHeight) / 2 + metrics.getAscent()) + PLACEMAT_DEFAULT_YOFFSET - yOffset;

            // Draw text
            g2d.drawString(name, x, y);
            g2d.dispose();

            // Save image
            saveFile(outputFile, image);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static Font loadFiddlestixOutline() {
        try {
            InputStream fontStream = Main.class.getResourceAsStream("/fonts/FiddlestixOutline.ttf");
            assert fontStream != null;
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream);

            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);

            return font;
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("Serif", Font.BOLD, 100);
        }
    }
    private static Font loadFiddlestixSolid() {
        try {
            InputStream fontStream = Main.class.getResourceAsStream("/fonts/FiddlestixSolid.ttf");
            assert fontStream != null;
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream);

            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);

            return font;
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("Serif", Font.BOLD, 100);
        }
    }

    private static Font deriveFont(Font font, float size) {
        Font textFont = font.deriveFont(size);
        textFont = textFont.deriveFont(Map.of(TextAttribute.TRACKING, 0.25));

        return textFont;
    }

    private static Font loadCustomFont(float size, int type)
    {
        try {
            /*Font font = Font.createFont(
                    Font.TRUETYPE_FONT,
                    new File("src/main/resources/fonts/FiddlestixOutline.ttf")
            );*/
            InputStream fontStream = Main.class.getResourceAsStream("/fonts/FiddlestixOutline.ttf");
            if (type == 2) {
                fontStream = Main.class.getResourceAsStream("/fonts/FiddlestixSolid.ttf");
            }

            assert fontStream != null;
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream);

            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);

            return font.deriveFont(size);

        } catch (Exception e) {
            e.printStackTrace();
            return new Font("Serif", Font.BOLD, (int) size);
        }
    }

    private static int[] setBowlOffsets() {
        //SKUs start at 800 and run through 1800, range of 1000

        //bowl Y offsets
        int[] offsetsB = new int[1000];
        offsetsB[43] = 530;
        offsetsB[51] = 600;
        offsetsB[58] = -50;
        offsetsB[67] = 650;
        offsetsB[88] = 50;
        offsetsB[86] = 50;

        return offsetsB;
    }

    private static int[] setPlateOffsets() {
        //plate Y offsets
        int[] offsetsP = new int[1000];
        offsetsP[43] = 850;
        offsetsP[51] = 950;
        offsetsP[58] = -50;
        offsetsP[67] = 950;
        offsetsP[80] = -50;
        offsetsP[88] = 150;
        offsetsP[86] = 170;

        return offsetsP;
    }

    private static int[] setMugOffsets() {
        //mug Y offsets
        int[] offsetsM = new int[1000];
        offsetsM[35] = 0;
        offsetsM[43] = 300;
        offsetsM[58] = -50;
        offsetsM[67] = 365;

        return offsetsM;
    }

    private static int[] setUtensilsOffsets() {
        //utensils Y offsets
        int[] offsetsU = new int[1000];

        return offsetsU;
    }

    private static int[] setPlacematOffsets()
    {
        //placemat Y offsets
        int[] offsetsPM = new int[1000];
        offsetsPM[1] = 150;
        offsetsPM[2] = -810;
        offsetsPM[3] = -810;
        offsetsPM[4] = -810;
        offsetsPM[5] = -1535;
        offsetsPM[6] = -300;
        offsetsPM[9] = -1535;
        offsetsPM[12] = -810;
        offsetsPM[14] = -1785;
        offsetsPM[18] = -1900;
        offsetsPM[27] = 300;
        offsetsPM[28] = 300;
        offsetsPM[31] = -1785;
        offsetsPM[32] = -1535;
        offsetsPM[34] = -1785;
        offsetsPM[35] = -1785;
        offsetsPM[37] = -1485;
        offsetsPM[38] = -810;
        offsetsPM[41] = -1485;
        offsetsPM[42] = 250;
        offsetsPM[50] = -1785;
        offsetsPM[51] = -230;
        offsetsPM[53] = -1535;
        offsetsPM[54] = -1535;
        offsetsPM[55] = -1485;
        offsetsPM[59] = -1535;
        offsetsPM[63] = -1485;
        offsetsPM[65] = -1485;
        offsetsPM[68] = -1535;
        offsetsPM[70] = -1485;
        offsetsPM[72] = -1535;
        offsetsPM[73] = -1535;
        offsetsPM[78] = -1485;
        offsetsPM[81] = -1485;
        offsetsPM[82] = -1485;
        offsetsPM[84] = -1485;
        offsetsPM[85] = -1485;
        offsetsPM[86] = -1485;
        offsetsPM[87] = -1485;
        offsetsPM[88] = -1235;
        offsetsPM[89] = -1485;
        offsetsPM[90] = -1485;
        offsetsPM[93] = -1535;
        offsetsPM[94] = -1485;
        offsetsPM[95] = -1485;
        offsetsPM[96] = -1485;
        offsetsPM[97] = -1485;
        offsetsPM[98] = -1485;
        offsetsPM[99] = -1485;
        offsetsPM[100] = -1485;
        offsetsPM[101] = -1485;
        offsetsPM[102] = -1485;
        offsetsPM[103] = -1485;
        offsetsPM[900] = -1485;
        offsetsPM[902] = -1535;
        offsetsPM[903] = -1535;
        offsetsPM[908] = -1535;
        offsetsPM[910] = -1485;
        offsetsPM[912] = -1485;

        return offsetsPM;
    }

    private static int getYoffset(int id, int type) {
        if (type == TYPE_BOWL) {
            return offsetsB[id - 800];
        }
        else if (type == TYPE_PLATE) {
            return offsetsP[id - 800];
        }
        else if (type == TYPE_MUG) {
            return offsetsM[id - 800];
        }
        else if (type == TYPE_UTENSILS) {
            return offsetsU[id - 800];
        }
        else if (type == TYPE_PLACEMAT) {
            return offsetsPM[id - 800];
        }
        else {
            return 0;
        }
    }

    private static String[] setUtensilsColor()
    {
        String[] UtensilsColors = new String[1000];
        UtensilsColors[1] = "#4182c4";
        UtensilsColors[2] = "#9f77b4";
        UtensilsColors[3] = "#f06ea9";
        UtensilsColors[4] = "#0063b0";
        UtensilsColors[5] = "#f06eaa";
        UtensilsColors[6] = "#f78d31";
        UtensilsColors[9] = "#bd7cb5";
        UtensilsColors[11] = "#b16a0f";
        UtensilsColors[12] = "#d72229";
        UtensilsColors[13] = "#231f20";
        UtensilsColors[14] = "#92c960";
        UtensilsColors[18] = "#44c8f5";
        UtensilsColors[27] = "#e93f2e";
        UtensilsColors[28] = "#eb72ac";
        UtensilsColors[29] = "#003f86";
        UtensilsColors[30] = "#ef5ba1";
        UtensilsColors[31] = "#003e86";
        UtensilsColors[32] = "#369d49";
        UtensilsColors[34] = "#ef5ba1";
        UtensilsColors[35] = "#f173ac";
        UtensilsColors[37] = "#231f20";
        UtensilsColors[38] = "#ef59a1";
        UtensilsColors[41] = "#004a8b";
        UtensilsColors[42] = "#f7931d";
        UtensilsColors[43] = "#f068a7";
        UtensilsColors[50] = "#f172ac";
        UtensilsColors[51] = "#135b9e";
        UtensilsColors[52] = "#965ca6";
        UtensilsColors[53] = "#ef59a1";
        UtensilsColors[54] = "#ed1c24";
        UtensilsColors[55] = "#231f20";
        UtensilsColors[58] = "#0083cb";
        UtensilsColors[59] = "#1c4583";
        UtensilsColors[62] = "#22b24c";
        UtensilsColors[63] = "#ee3897";
        UtensilsColors[64] = "#0083cb";
        UtensilsColors[65] = "#818285";
        UtensilsColors[67] = "#2e6ab3";
        UtensilsColors[68] = "#88c86f";
        UtensilsColors[70] = "#f27cb1";
        UtensilsColors[72] = "#ce171f";
        UtensilsColors[73] = "#0054a6";
        UtensilsColors[74] = "#004c8e";
        UtensilsColors[75] = "#231f20";
        UtensilsColors[76] = "#f067a6";
        UtensilsColors[77] = "#f26522";
        UtensilsColors[78] = "#0054a6";
        UtensilsColors[79] = "#0063b0";
        UtensilsColors[80] = "#33b560";
        UtensilsColors[81] = "#69b3e4";
        UtensilsColors[82] = "#7c51a1";
        UtensilsColors[83] = "#f06799";
        UtensilsColors[84] = "#231f20";
        UtensilsColors[85] = "#0baf4d";
        UtensilsColors[86] = "#0baf4d";
        UtensilsColors[87] = "#68c184";
        UtensilsColors[88] = "#ef4c8f";
        UtensilsColors[89] = "#2384c6";
        UtensilsColors[90] = "#369d49";
        UtensilsColors[93] = "#d71921";
        UtensilsColors[94] = "#33b560";
        UtensilsColors[95] = "#ef5ba1";
        UtensilsColors[96] = "#47c7ec";
        UtensilsColors[97] = "#1c4583";
        UtensilsColors[99] = "#67c18c";
        UtensilsColors[100] = "#8ed8f8";
        UtensilsColors[101] = "#659d6b";
        UtensilsColors[102] = "#396ea4";
        UtensilsColors[103] = "#326ab3";
        UtensilsColors[900] = "#ed1c24";
        UtensilsColors[902] = "#d71921";
        UtensilsColors[903] = "#ed1c24";
        UtensilsColors[906] = "#d71921";
        UtensilsColors[908] = "#ed1c24";
        UtensilsColors[910] = "#d71921";
        UtensilsColors[912] = "#cf1b22";

        return UtensilsColors;
    }

    private static Color getUtensilsColor(int bgID)
    {
        return Color.decode(UtensilsColors[bgID-800]);
    }

    private static void saveFile(String outputFile, BufferedImage image) throws IOException {
        File output = new File(outputFile);
        output.getParentFile().mkdirs();

        ICC_Profile profile = ICC_Profile.getInstance(ColorSpace.CS_sRGB);
        byte[] profileData = profile.getData();

        // Encode the JPEG using TwelveMonkeys
        ByteArrayOutputStream jpegData = new ByteArrayOutputStream();

        ImageWriter writer =
                ImageIO.getImageWritersByFormatName("JPEG").next();

        try (ImageOutputStream outputStream =
                     ImageIO.createImageOutputStream(jpegData)) {

            writer.setOutput(outputStream);
            writer.write(image);

        } finally {
            writer.dispose();
        }

        byte[] jpeg = jpegData.toByteArray();

        // ICC APP2 segment
        byte[] identifier = "ICC_PROFILE\0".getBytes(java.nio.charset.StandardCharsets.US_ASCII);
        int segmentLength = 2 + identifier.length + 2 + profileData.length;

        try (FileOutputStream out = new FileOutputStream(output)) {

            // JPEG SOI
            out.write(jpeg, 0, 2);

            // APP2 marker
            out.write(0xFF);
            out.write(0xE2);

            // APP2 segment length
            out.write((segmentLength >> 8) & 0xFF);
            out.write(segmentLength & 0xFF);

            // ICC_PROFILE identifier
            out.write(identifier);

            // ICC profile chunk number and total chunks
            out.write(1);
            out.write(1);

            // sRGB ICC profile
            out.write(profileData);

            // Rest of JPEG
            out.write(jpeg, 2, jpeg.length - 2);
        }

        System.out.println("Image saved as " + output.getAbsolutePath());
    }

    private static BufferedImage createBufferedImage(int imageWidth, int imageHeight)
    {
        return new BufferedImage(
                imageWidth,
                imageHeight,
                BufferedImage.TYPE_INT_RGB
        );
    }
}
