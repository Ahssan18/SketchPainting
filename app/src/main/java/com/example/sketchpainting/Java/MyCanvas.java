package com.example.sketchpainting.Java;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.sketchpainting.Activities.TouchFillActivity;
import com.example.sketchpainting.Models.ModelCanvas;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MyCanvas extends View {
    public static List<ModelCanvas> listCanvasModel;
    public static List<ModelCanvas> undolistCanvasModel;
    public static int selectedColor = Color.BLACK;
    public static float selectedStroke = 10f;
    float mScaleFactor;
    Path transformedPath;
    Matrix drawMatrix;
    Context context;
    ModelCanvas modelCanvas;
    private Drawable mCustomImage = null;
    private Bitmap bitmap = null;
    private Bitmap myBitmap;
    private Paint paint;
    private float scaleFactor = 1.f;
    private String type = "";
    private Point p1;

    public MyCanvas(Context context, String type, int image) {
        super(context);
        drawMatrix = new Matrix();
        transformedPath = new Path();
        this.context = context;
        this.type = type;
        listCanvasModel = new ArrayList<>();
        undolistCanvasModel = new ArrayList<>();
        getPaintObject(selectedColor, selectedStroke);
        paint = new Paint();
        paint.setColor(Color.RED);
        if ((type.equalsIgnoreCase("touch_fill") || (type.equalsIgnoreCase("sketch_fill")))) {
            bitmap = BitmapFactory.decodeResource(getResources(), image);
            // Bitmap newBmp = Bitmap.createBitmap(R.drawable.test_sketch.getWidth(), R.drawable.test_sketch.getHeight(), Bitmap.Config.ARGB);
            myBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            //drawableToBitmap(R.drawable.test_sketch);

            //mCustomImage = context.getResources().getDrawable(R.drawable.test_sketch);
        }


    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

   /* @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

    }*/

    private Paint getPaintObject(int color, float stroke) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(stroke);
        return paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (ModelCanvas model : listCanvasModel)
            canvas.drawPath(model.getPath(), model.getPaint());
        Log.d("Type_s", type);
        if ((type.equalsIgnoreCase("touch_fill") || (type.equalsIgnoreCase("sketch_fill")))) {
//            mCustomImage.setBounds(canvas.getClipBounds());
//            mCustomImage.draw(canvas);
            float centreX = (canvas.getWidth() - bitmap.getWidth()) / 2;
            float centreY = (canvas.getHeight() - bitmap.getHeight()) / 2;
            canvas.drawBitmap(bitmap, centreX, centreY, paint);

        }
    }

    public void deleteAll() {
        listCanvasModel.clear();
        invalidate();
    }

    public void onClickUndo() {
        if (listCanvasModel.size() > 0) {
            undolistCanvasModel.add(listCanvasModel.remove(listCanvasModel.size() - 1));
            listCanvasModel.size();
            listCanvasModel.remove(listCanvasModel.size() - 1);
            invalidate();
        }
    }

    public void onClickRedo() {
        if (undolistCanvasModel.size() > 0) {
            listCanvasModel.add(undolistCanvasModel.remove(undolistCanvasModel.size() - 1));
            invalidate();
        } else {

        }
        //toast the user
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        p1 = new Point();
        p1.x = (int) x; //x co-ordinate where the user touches on the screen
        p1.y = (int) y;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                try {
                    if (this.context instanceof TouchFillActivity) {
                        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                        if (bitmap.isMutable()) {
//                            QueueLinearFloodFiller filler = new QueueLinearFloodFiller(bitmap, bitmap.getPixel(x, y), selectedColor);
//                            filler.setTolerance(10);
//                            filler.floodFill(p1.x, p1.y);
                            new MyAsync().execute();
                            invalidate();
                        } else {
                            Log.d("bitmap_status", "unmuted");
                        }
                    } else {
                        modelCanvas = new ModelCanvas();
                        modelCanvas.setPath(new Path());
                        modelCanvas.setPaint(getPaintObject(selectedColor, selectedStroke));
                        modelCanvas.getPath().moveTo(x, y);
                        invalidate();
                    }
                } catch (Exception e) {
                    modelCanvas = new ModelCanvas();
                    modelCanvas.setPath(new Path());
                    modelCanvas.setPaint(getPaintObject(selectedColor, selectedStroke));
                    modelCanvas.getPath().moveTo(x, y);
                    e.printStackTrace();
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                try {
                    if (this.context instanceof TouchFillActivity) {
                        invalidate();
                    } else {
                        modelCanvas.getPath().lineTo(x, y);
                        invalidate();
                    }
                } catch (Exception e) {
                    modelCanvas.getPath().lineTo(x, y);
                    e.printStackTrace();
                }
            case MotionEvent.ACTION_UP:

                try {
                    if (!(this.context instanceof TouchFillActivity)) {
                        listCanvasModel.add(modelCanvas);
                        invalidate();
                    }

                } catch (Exception e) {
                    Log.d("Action_dn", "Catch running in action down");
                    listCanvasModel.add(modelCanvas);
                    e.printStackTrace();
                }
                break;
            default:
                return false;
        }
        return true;
    }

    public class MyAsync extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            floodFill(bitmap, new Point(p1.x, p1.y), bitmap.getPixel(p1.x, p1.y), selectedColor);
            return null;
        }
    }

    public void floodFill(Bitmap image, Point node, int targetColor, int replacementColor) {
        int width = image.getWidth();
        int height = image.getHeight();
        int target = targetColor;
        int replacement = replacementColor;
        if (target != replacement) {
            Queue<Point> queue = new LinkedList<Point>();
            do {
                int x = node.x;
                int y = node.y;
                while (x > 0 && image.getPixel(x - 1, y) == target) {
                    x--;
                }
                boolean spanUp = false;
                boolean spanDown = false;
                while (x < width && image.getPixel(x, y) == target) {
                    image.setPixel(x, y, replacement);
                    if (!spanUp && y > 0 && image.getPixel(x, y - 1) == target) {
                        queue.add(new Point(x, y - 1));
                        spanUp = true;
                    } else if (spanUp && y > 0
                            && image.getPixel(x, y - 1) != target) {
                        spanUp = false;
                    }
                    if (!spanDown && y < height - 1
                            && image.getPixel(x, y + 1) == target) {
                        queue.add(new Point(x, y + 1));
                        spanDown = true;
                    } else if (spanDown && y < height - 1
                            && image.getPixel(x, y + 1) != target) {
                        spanDown = false;
                    }
                    x++;
                }
            } while ((node = queue.poll()) != null);
        }
    }

    public class QueueLinearFloodFiller {
        protected Bitmap image = null;
        protected int[] tolerance = new int[]{0, 0, 0};
        protected int width = 0;
        protected int height = 0;
        protected int[] pixels = null;
        protected int fillColor = 0;
        protected int[] startColor = new int[]{0, 0, 0};
        protected boolean[] pixelsChecked;
        protected Queue<FloodFillRange> ranges;

        // Construct using an image and a copy will be made to fill into,
        // Construct with BufferedImage and flood fill will write directly to
        // provided BufferedImage
        public QueueLinearFloodFiller(Bitmap img) {
            copyImage(img);
        }

        public QueueLinearFloodFiller(Bitmap img, int targetColor, int newColor) {
            useImage(img);

            setFillColor(newColor);
            setTargetColor(targetColor);
        }

        public void setTargetColor(int targetColor) {
            startColor[0] = Color.red(targetColor);
            startColor[1] = Color.green(targetColor);
            startColor[2] = Color.blue(targetColor);
        }

        public int getFillColor() {
            return fillColor;
        }

        public void setFillColor(int value) {
            fillColor = value;
        }

        public int[] getTolerance() {
            return tolerance;
        }

        public void setTolerance(int[] value) {
            tolerance = value;
        }

        public void setTolerance(int value) {
            tolerance = new int[]{value, value, value};
        }

        public Bitmap getImage() {
            return image;
        }

        public void copyImage(Bitmap img) {
            // Copy data from provided Image to a BufferedImage to write flood fill
            // to, use getImage to retrieve
            // cache data in member variables to decrease overhead of property calls
            width = img.getWidth();
            height = img.getHeight();

            image = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(image);
            canvas.drawBitmap(img, 0, 0, null);

            pixels = new int[width * height];

            image.getPixels(pixels, 0, width, 1, 1, width - 1, height - 1);
        }

        public void useImage(Bitmap img) {
            // Use a pre-existing provided BufferedImage and write directly to it
            // cache data in member variables to decrease overhead of property calls
            width = img.getWidth();
            height = img.getHeight();
            image = img;

            pixels = new int[width * height];

            image.getPixels(pixels, 0, width, 1, 1, width - 1, height - 1);
        }

        protected void prepare() {
            // Called before starting flood-fill
            pixelsChecked = new boolean[pixels.length];
            ranges = new LinkedList<FloodFillRange>();
        }

        // Fills the specified point on the bitmap with the currently selected fill
        // color.
        // int x, int y: The starting coords for the fill
        public void floodFill(int x, int y) {
            // Setup
            prepare();

            if (startColor[0] == 0) {
                // ***Get starting color.
                int startPixel = pixels[(width * y) + x];
                startColor[0] = (startPixel >> 16) & 0xff;
                startColor[1] = (startPixel >> 8) & 0xff;
                startColor[2] = startPixel & 0xff;
            }

            // ***Do first call to floodfill.
            LinearFill(x, y);

            // ***Call floodfill routine while floodfill ranges still exist on the
            // queue
            FloodFillRange range;

            while (ranges.size() > 0) {
                // **Get Next Range Off the Queue
                range = ranges.remove();

                // **Check Above and Below Each Pixel in the Floodfill Range
                int downPxIdx = (width * (range.Y + 1)) + range.startX;
                int upPxIdx = (width * (range.Y - 1)) + range.startX;
                int upY = range.Y - 1;// so we can pass the y coord by ref
                int downY = range.Y + 1;

                for (int i = range.startX; i <= range.endX; i++) {
                    // *Start Fill Upwards
                    // if we're not above the top of the bitmap and the pixel above
                    // this one is within the color tolerance
                    if (range.Y > 0 && (!pixelsChecked[upPxIdx])
                            && CheckPixel(upPxIdx))
                        LinearFill(i, upY);

                    // *Start Fill Downwards
                    // if we're not below the bottom of the bitmap and the pixel
                    // below this one is within the color tolerance
                    if (range.Y < (height - 1) && (!pixelsChecked[downPxIdx])
                            && CheckPixel(downPxIdx))
                        LinearFill(i, downY);

                    downPxIdx++;
                    upPxIdx++;
                }
            }

            image.setPixels(pixels, 0, width, 1, 1, width - 1, height - 1);
        }

        // Finds the furthermost left and right boundaries of the fill area
        // on a given y coordinate, starting from a given x coordinate, filling as
        // it goes.
        // Adds the resulting horizontal range to the queue of floodfill ranges,
        // to be processed in the main loop.

        // int x, int y: The starting coords
        protected void LinearFill(int x, int y) {
            // ***Find Left Edge of Color Area
            int lFillLoc = x; // the location to check/fill on the left
            int pxIdx = (width * y) + x;

            while (true) {
                // **fill with the color
                pixels[pxIdx] = fillColor;

                // **indicate that this pixel has already been checked and filled
                pixelsChecked[pxIdx] = true;

                // **de-increment
                lFillLoc--; // de-increment counter
                pxIdx--; // de-increment pixel index

                // **exit loop if we're at edge of bitmap or color area
                if (lFillLoc < 0 || (pixelsChecked[pxIdx]) || !CheckPixel(pxIdx)) {
                    break;
                }
            }

            lFillLoc++;

            // ***Find Right Edge of Color Area
            int rFillLoc = x; // the location to check/fill on the left

            pxIdx = (width * y) + x;

            while (true) {
                // **fill with the color
                pixels[pxIdx] = fillColor;

                // **indicate that this pixel has already been checked and filled
                pixelsChecked[pxIdx] = true;

                // **increment
                rFillLoc++; // increment counter
                pxIdx++; // increment pixel index

                // **exit loop if we're at edge of bitmap or color area
                if (rFillLoc >= width || pixelsChecked[pxIdx] || !CheckPixel(pxIdx)) {
                    break;
                }
            }

            rFillLoc--;

            // add range to queue
            FloodFillRange r = new FloodFillRange(lFillLoc, rFillLoc, y);

            ranges.offer(r);
        }

        // Sees if a pixel is within the color tolerance range.
        protected boolean CheckPixel(int px) {
            int red = (pixels[px] >>> 16) & 0xff;
            int green = (pixels[px] >>> 8) & 0xff;
            int blue = pixels[px] & 0xff;

            return (red >= (startColor[0] - tolerance[0])
                    && red <= (startColor[0] + tolerance[0])
                    && green >= (startColor[1] - tolerance[1])
                    && green <= (startColor[1] + tolerance[1])
                    && blue >= (startColor[2] - tolerance[2]) && blue <= (startColor[2] + tolerance[2]));
        }

        // Represents a linear range to be filled and branched from.
        protected class FloodFillRange {
            public int startX;
            public int endX;
            public int Y;

            public FloodFillRange(int startX, int endX, int y) {
                this.startX = startX;
                this.endX = endX;
                this.Y = y;
            }
        }
    }
}
