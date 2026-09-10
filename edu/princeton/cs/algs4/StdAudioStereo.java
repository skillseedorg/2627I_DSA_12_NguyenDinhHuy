package edu.princeton.cs.algs4;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.AudioFileFormat.Type;

public final class StdAudioStereo {
   public static final int SAMPLE_RATE = 44100;
   private static final int BYTES_PER_SAMPLE = 2;
   private static final int BITS_PER_SAMPLE = 16;
   private static final int MAX_16_BIT = 32768;
   private static final int BYTES_PER_FRAME = 4;
   private static final int SAMPLE_BUFFER_SIZE = 4096;
   private static final int MONAURAL = 1;
   private static final int STEREO = 2;
   private static final boolean LITTLE_ENDIAN = false;
   private static final boolean BIG_ENDIAN = true;
   private static final boolean UNSIGNED = false;
   private static final boolean SIGNED = true;
   private static final boolean LEFT_CHANNEL = false;
   private static final boolean RIGHT_CHANNEL = true;
   private static SourceDataLine line;
   private static byte[] buffer;
   private static int bufferSize = 0;
   private static LinkedList<BackgroundRunnable> backgroundRunnables = new LinkedList();
   private static QueueOfDoubles recordedSamplesLeft = null;
   private static QueueOfDoubles recordedSamplesRight = null;
   private static boolean isRecording = false;

   private StdAudioStereo() {
   }

   private static void init() {
      try {
         AudioFormat format = new AudioFormat(44100.0F, 16, 2, true, false);
         DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
         line = (SourceDataLine)AudioSystem.getLine(info);
         line.open(format, 8192);
         buffer = new byte[4096];
      } catch (LineUnavailableException e) {
         System.out.println(e.getMessage());
      }

      line.start();
   }

   private static AudioInputStream getAudioInputStreamFromFile(String filename) {
      if (filename == null) {
         throw new IllegalArgumentException("filename is null");
      } else {
         try {
            File file = new File(filename);
            if (file.exists()) {
               return AudioSystem.getAudioInputStream(file);
            } else {
               InputStream is1 = StdAudioStereo.class.getResourceAsStream(filename);
               if (is1 != null) {
                  return AudioSystem.getAudioInputStream(is1);
               } else {
                  InputStream is2 = StdAudioStereo.class.getClassLoader().getResourceAsStream(filename);
                  if (is2 != null) {
                     return AudioSystem.getAudioInputStream(is2);
                  } else {
                     URI uri = new URI(filename);
                     if (uri.isAbsolute()) {
                        URL url = uri.toURL();
                        return AudioSystem.getAudioInputStream(url);
                     } else {
                        throw new IllegalArgumentException("could not read audio file '" + filename + "'");
                     }
                  }
               }
            }
         } catch (URISyntaxException | IOException var6) {
            throw new IllegalArgumentException("could not read audio file '" + filename + "'");
         } catch (UnsupportedAudioFileException e) {
            throw new IllegalArgumentException("file of unsupported audio file format: '" + filename + "'", e);
         }
      }
   }

   public static void drain() {
      if (bufferSize > 0) {
         line.write(buffer, 0, bufferSize);
         bufferSize = 0;
      }

      line.drain();
   }

   public static void play(double sampleLeft, double sampleRight) {
      if (Double.isNaN(sampleLeft)) {
         throw new IllegalArgumentException("sampleLeft is NaN");
      } else if (Double.isNaN(sampleRight)) {
         throw new IllegalArgumentException("sampleRight is NaN");
      } else {
         if (sampleLeft < (double)-1.0F) {
            sampleLeft = (double)-1.0F;
         }

         if (sampleLeft > (double)1.0F) {
            sampleLeft = (double)1.0F;
         }

         if (sampleRight < (double)-1.0F) {
            sampleRight = (double)-1.0F;
         }

         if (sampleRight > (double)1.0F) {
            sampleRight = (double)1.0F;
         }

         if (isRecording) {
            recordedSamplesLeft.enqueue(sampleLeft);
            recordedSamplesRight.enqueue(sampleRight);
         }

         short sLeft = (short)((int)((double)32768.0F * sampleLeft));
         if (sampleLeft == (double)1.0F) {
            sLeft = 32767;
         }

         buffer[bufferSize++] = (byte)sLeft;
         buffer[bufferSize++] = (byte)(sLeft >> 8);
         short sRight = (short)((int)((double)32768.0F * sampleRight));
         if (sampleRight == (double)1.0F) {
            sRight = 32767;
         }

         buffer[bufferSize++] = (byte)sRight;
         buffer[bufferSize++] = (byte)(sRight >> 8);
         if (bufferSize >= buffer.length) {
            line.write(buffer, 0, buffer.length);
            bufferSize = 0;
         }

      }
   }

   public static void play(double sample) {
      play(sample, sample);
   }

   public static void play(double[] samples) {
      if (samples == null) {
         throw new IllegalArgumentException("argument to play() is null");
      } else {
         for(int i = 0; i < samples.length; ++i) {
            play(samples[i]);
         }

      }
   }

   public static void play(double[] samplesLeft, double[] samplesRight) {
      if (samplesLeft == null) {
         throw new IllegalArgumentException("argument to play() is null");
      } else if (samplesRight == null) {
         throw new IllegalArgumentException("argument to play() is null");
      } else if (samplesLeft.length != samplesRight.length) {
         throw new IllegalArgumentException("left and right arrays have different lengths");
      } else {
         for(int i = 0; i < samplesLeft.length; ++i) {
            play(samplesLeft[i], samplesRight[i]);
         }

      }
   }

   public static void play(String filename) {
      if (isRecording) {
         double[] samplesLeft = readLeft(filename);
         double[] samplesRight = readRight(filename);

         for(double sample : samplesLeft) {
            recordedSamplesLeft.enqueue(sample);
         }

         for(double sample : samplesRight) {
            recordedSamplesRight.enqueue(sample);
         }
      }

      AudioInputStream ais = getAudioInputStreamFromFile(filename);
      SourceDataLine line = null;
      int BUFFER_SIZE = 4096;

      try {
         AudioFormat audioFormat = ais.getFormat();
         DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
         line = (SourceDataLine)AudioSystem.getLine(info);
         line.open(audioFormat);
         line.start();
         byte[] samples = new byte[BUFFER_SIZE];
         int count = 0;

         while((count = ais.read(samples, 0, BUFFER_SIZE)) != -1) {
            line.write(samples, 0, count);
         }
      } catch (LineUnavailableException | IOException e) {
         System.out.println(e);
      } finally {
         if (line != null) {
            line.drain();
            line.close();
         }

      }

   }

   private static double[] readChannel(String filename, boolean channel) {
      int READ_BUFFER_SIZE = 4096;
      AudioInputStream fromAudioInputStream = getAudioInputStreamFromFile(filename);
      AudioFormat fromAudioFormat = fromAudioInputStream.getFormat();
      AudioFormat toAudioFormat = new AudioFormat(44100.0F, 16, 2, true, false);
      if (!AudioSystem.isConversionSupported(toAudioFormat, fromAudioFormat)) {
         throw new IllegalArgumentException("system cannot convert from " + fromAudioFormat + " to " + toAudioFormat);
      } else {
         AudioInputStream toAudioInputStream = AudioSystem.getAudioInputStream(toAudioFormat, fromAudioInputStream);

         try {
            QueueOfDoubles queue = new QueueOfDoubles();
            int count = 0;
            byte[] bytes = new byte[READ_BUFFER_SIZE];

            while((count = toAudioInputStream.read(bytes, 0, READ_BUFFER_SIZE)) != -1) {
               for(int i = 0; i < count / 4; ++i) {
                  double left = (double)((short)((bytes[4 * i + 1] & 255) << 8 | bytes[4 * i + 0] & 255)) / (double)32768.0F;
                  double right = (double)((short)((bytes[4 * i + 3] & 255) << 8 | bytes[4 * i + 2] & 255)) / (double)32768.0F;
                  if (!channel) {
                     queue.enqueue(left);
                  } else {
                     queue.enqueue(right);
                  }
               }
            }

            toAudioInputStream.close();
            fromAudioInputStream.close();
            return queue.toArray();
         } catch (IOException ioe) {
            throw new IllegalArgumentException("could not read audio file '" + filename + "'", ioe);
         }
      }
   }

   public static double[] readLeft(String filename) {
      return readChannel(filename, false);
   }

   public static double[] readRight(String filename) {
      return readChannel(filename, true);
   }

   public static double[] read(String filename) {
      int READ_BUFFER_SIZE = 4096;
      AudioInputStream fromAudioInputStream = getAudioInputStreamFromFile(filename);
      AudioFormat fromAudioFormat = fromAudioInputStream.getFormat();
      AudioFormat toAudioFormat = new AudioFormat(44100.0F, 16, 1, true, false);
      if (!AudioSystem.isConversionSupported(toAudioFormat, fromAudioFormat)) {
         throw new IllegalArgumentException("system cannot convert from " + fromAudioFormat + " to " + toAudioFormat);
      } else {
         AudioInputStream toAudioInputStream = AudioSystem.getAudioInputStream(toAudioFormat, fromAudioInputStream);

         try {
            QueueOfDoubles queue = new QueueOfDoubles();
            int count = 0;
            byte[] bytes = new byte[READ_BUFFER_SIZE];

            while((count = toAudioInputStream.read(bytes, 0, READ_BUFFER_SIZE)) != -1) {
               for(int i = 0; i < count / 2; ++i) {
                  double sample = (double)((short)((bytes[2 * i + 1] & 255) << 8 | bytes[2 * i] & 255)) / (double)32768.0F;
                  queue.enqueue(sample);
               }
            }

            toAudioInputStream.close();
            fromAudioInputStream.close();
            return queue.toArray();
         } catch (IOException ioe) {
            throw new IllegalArgumentException("could not read audio file '" + filename + "'", ioe);
         }
      }
   }

   private static void save(String filename, byte[] data, AudioFormat format, long numberOfFrames) {
      try {
         ByteArrayInputStream bais = new ByteArrayInputStream(data);

         try {
            AudioInputStream ais = new AudioInputStream(bais, format, numberOfFrames);

            try {
               if (!filename.endsWith(".wav") && !filename.endsWith(".WAV")) {
                  if (!filename.endsWith(".au") && !filename.endsWith(".AU")) {
                     if (!filename.endsWith(".aif") && !filename.endsWith(".aiff") && !filename.endsWith(".AIF") && !filename.endsWith(".AIFF")) {
                        throw new IllegalArgumentException("file extension for saving must be .wav, .au, or .aif");
                     }

                     if (!AudioSystem.isFileTypeSupported(Type.AIFF, ais)) {
                        throw new IllegalArgumentException("saving to AIFF file format is not supported on this system");
                     }

                     AudioSystem.write(ais, Type.AIFF, new File(filename));
                  } else {
                     if (!AudioSystem.isFileTypeSupported(Type.AU, ais)) {
                        throw new IllegalArgumentException("saving to AU file format is not supported on this system");
                     }

                     AudioSystem.write(ais, Type.AU, new File(filename));
                  }
               } else {
                  if (!AudioSystem.isFileTypeSupported(Type.WAVE, ais)) {
                     throw new IllegalArgumentException("saving to WAVE file format is not supported on this system");
                  }

                  AudioSystem.write(ais, Type.WAVE, new File(filename));
               }
            } catch (Throwable var11) {
               try {
                  ais.close();
               } catch (Throwable var10) {
                  var11.addSuppressed(var10);
               }

               throw var11;
            }

            ais.close();
         } catch (Throwable var12) {
            try {
               bais.close();
            } catch (Throwable var9) {
               var12.addSuppressed(var9);
            }

            throw var12;
         }

         bais.close();
      } catch (IOException ioe) {
         throw new IllegalArgumentException("unable to save file '" + filename + "'", ioe);
      }
   }

   public static void save(String filename, double[] samples) {
      if (filename == null) {
         throw new IllegalArgumentException("filename is null");
      } else if (samples == null) {
         throw new IllegalArgumentException("samples[] is null");
      } else if (filename.length() == 0) {
         throw new IllegalArgumentException("argument to save() is the empty string");
      } else {
         AudioFormat format = new AudioFormat(44100.0F, 16, 1, true, false);
         byte[] data = new byte[2 * samples.length];

         for(int i = 0; i < samples.length; ++i) {
            int temp = (short)((int)(samples[i] * (double)32768.0F));
            if (samples[i] == (double)1.0F) {
               temp = 32767;
            }

            data[2 * i + 0] = (byte)temp;
            data[2 * i + 1] = (byte)(temp >> 8);
         }

         save(filename, data, format, (long)samples.length);
      }
   }

   public static void save(String filename, double[] samplesLeft, double[] samplesRight) {
      if (filename == null) {
         throw new IllegalArgumentException("filename is null");
      } else if (samplesLeft == null) {
         throw new IllegalArgumentException("samplesLeft[] is null");
      } else if (samplesRight == null) {
         throw new IllegalArgumentException("samplesRight[] is null");
      } else if (samplesLeft.length != samplesRight.length) {
         throw new IllegalArgumentException("input arrays have different lengths");
      } else {
         AudioFormat format = new AudioFormat(44100.0F, 16, 2, true, false);
         byte[] data = new byte[4 * samplesLeft.length];

         for(int i = 0; i < samplesLeft.length; ++i) {
            int tempLeft = (short)((int)(samplesLeft[i] * (double)32768.0F));
            int tempRight = (short)((int)(samplesRight[i] * (double)32768.0F));
            if (samplesLeft[i] == (double)1.0F) {
               tempLeft = 32767;
            }

            if (samplesRight[i] == (double)1.0F) {
               tempRight = 32767;
            }

            data[4 * i + 0] = (byte)tempLeft;
            data[4 * i + 1] = (byte)(tempLeft >> 8);
            data[4 * i + 2] = (byte)tempRight;
            data[4 * i + 3] = (byte)(tempRight >> 8);
         }

         save(filename, data, format, (long)samplesLeft.length);
      }
   }

   public static synchronized void stopInBackground() {
      for(BackgroundRunnable runnable : backgroundRunnables) {
         runnable.stop();
      }

      backgroundRunnables.clear();
   }

   public static synchronized void playInBackground(String filename) {
      BackgroundRunnable runnable = new BackgroundRunnable(filename);
      (new Thread(runnable)).start();
      backgroundRunnables.add(runnable);
   }

   /** @deprecated */
   @Deprecated
   public static synchronized void loopInBackground(String filename) {
      if (filename == null) {
         throw new IllegalArgumentException();
      } else {
         AudioInputStream ais = getAudioInputStreamFromFile(filename);

         try {
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.loop(-1);
         } catch (LineUnavailableException | IOException e) {
            System.out.println(e);
         }

         (new Thread(new Runnable() {
            public void run() {
               while(true) {
                  try {
                     Thread.sleep(1000L);
                  } catch (InterruptedException e) {
                     System.out.println(e);
                  }
               }
            }
         })).start();
      }
   }

   public static void startRecording() {
      if (!isRecording) {
         recordedSamplesLeft = new QueueOfDoubles();
         recordedSamplesRight = new QueueOfDoubles();
         isRecording = true;
      } else {
         throw new IllegalStateException("startRecording() must not be called twice in a row");
      }
   }

   public static void stopRecording() {
      if (isRecording) {
         isRecording = false;
      } else {
         throw new IllegalStateException("stopRecording() must be called after calling startRecording()");
      }
   }

   public static double[] getRecordingLeft() {
      if (recordedSamplesRight != null) {
         throw new IllegalStateException("getRecordingLeft() must be called after startRecording()");
      } else if (isRecording) {
         throw new IllegalStateException("getRecordingLeft() must be called after stopRecording()");
      } else {
         double[] results = recordedSamplesLeft.toArray();
         return results;
      }
   }

   public static double[] getRecordingRight() {
      if (recordedSamplesRight != null) {
         throw new IllegalStateException("getRecordingRight() must be called after startRecording()");
      } else if (isRecording) {
         throw new IllegalStateException("getRecordingRight() must be called after stopRecording()");
      } else {
         double[] result = recordedSamplesRight.toArray();
         return result;
      }
   }

   public static double[] getRecordingMono() {
      if (recordedSamplesRight != null) {
         throw new IllegalStateException("getRecording() must be called after startRecording()");
      } else if (isRecording) {
         throw new IllegalStateException("getRecording() must be called after stopRecording()");
      } else {
         double[] left = getRecordingLeft();
         double[] right = getRecordingRight();
         int n = left.length;
         double[] sum = new double[n];

         for(int i = 0; i < left.length; ++i) {
            sum[i] = (double)0.5F * (left[i] + right[i]);
         }

         return sum;
      }
   }

   public static void main(String[] args) {
      double freq1 = (double)440.0F;
      double freq2 = (double)220.0F;

      for(int i = 0; i <= 44100; ++i) {
         double sampleLeft = (double)0.5F * Math.sin((Math.PI * 2D) * freq1 * (double)i / (double)44100.0F);
         double sampleRight = (double)0.5F * Math.sin((Math.PI * 2D) * freq2 * (double)i / (double)44100.0F);
         play(sampleLeft, sampleRight);
      }

      String base = "https://introcs.cs.princeton.edu/java/stdlib/";
      play(base + "test.wav");
      play(readLeft(base + "test.midi"), readRight(base + "test.midi"));

      for(int i = 0; i < 10; ++i) {
         play(base + "BassDrum.wav");
         play(base + "SnareDrum.wav");
      }

      drain();
   }

   static {
      init();
   }

   private static class BackgroundRunnable implements Runnable {
      private volatile boolean exit = false;
      private final String filename;

      public BackgroundRunnable(String filename) {
         this.filename = filename;
      }

      public void run() {
         AudioInputStream ais = StdAudioStereo.getAudioInputStreamFromFile(this.filename);
         SourceDataLine line = null;
         int BUFFER_SIZE = 4096;

         try {
            AudioFormat audioFormat = ais.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            line = (SourceDataLine)AudioSystem.getLine(info);
            line.open(audioFormat);
            line.start();
            byte[] samples = new byte[BUFFER_SIZE];
            int count = 0;

            while(!this.exit && (count = ais.read(samples, 0, BUFFER_SIZE)) != -1) {
               line.write(samples, 0, count);
            }
         } catch (LineUnavailableException | IOException e) {
            System.out.println(e);
         } finally {
            if (line != null) {
               line.drain();
               line.close();
            }

            StdAudioStereo.backgroundRunnables.remove(this);
         }

      }

      public void stop() {
         this.exit = true;
      }
   }

   private static class QueueOfDoubles {
      private static final int INIT_CAPACITY = 16;
      private double[] a = new double[16];
      private int n = 0;

      public QueueOfDoubles() {
      }

      private void resize(int capacity) {
         assert capacity >= this.n;

         double[] temp = new double[capacity];

         for(int i = 0; i < this.n; ++i) {
            temp[i] = this.a[i];
         }

         this.a = temp;
      }

      public void enqueue(double item) {
         if (this.n == this.a.length) {
            this.resize(2 * this.a.length);
         }

         this.a[this.n++] = item;
      }

      public int size() {
         return this.n;
      }

      public double[] toArray() {
         double[] result = new double[this.n];

         for(int i = 0; i < this.n; ++i) {
            result[i] = this.a[i];
         }

         return result;
      }
   }
}
