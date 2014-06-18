#include <string.h>
#include <jni.h>
#include <android/log.h>
#include <fcntl.h>

#define MAX_DIGIT 4
#define LINE_BUFF 16
#define MAX_BUFF 32
#define FPGA_TEXT_LCD_DEVICE "/dev/fpga_text_lcd"
#define LED_DEVICE "/dev/fpga_led"
#define FND_DEVICE "/dev/fpga_fnd"

int fpga_fnd(const char* str)
{
	int dev;
	unsigned char data[4];
	int i, j = 0;
	int str_size;

	memset(data,0,sizeof(data));

	str_size=(strlen(str));
	if(str_size>MAX_DIGIT) {
		str_size=MAX_DIGIT;
	}

	for(i=0;i<MAX_DIGIT-str_size;i++) {
		data[i]='0';
	}

	for(;i<MAX_DIGIT;i++) {
		if((str[j]<0x30)||(str[j])>0x39) {
			return 1;
		}
		data[i]=str[j++]-0x30;
	}

	dev = open(FND_DEVICE, O_RDWR);
	if (dev<0) {
		__android_log_print(ANDROID_LOG_INFO, "Device Open Error", "Driver = %s", str);
		return -1;
	} else {
		__android_log_print(ANDROID_LOG_INFO, "Device Open Success", "Driver = %d", str);
		write(dev,&data,4);
		fpga_text_lcd(str);
		fpga_led();
		close(dev);
		return 0;
	}
}

int fpga_text_lcd(const char* str)
{
	int i;
	int dev;
	int str_size;
	unsigned char string[32];

	memset(string,0,sizeof(string));

	dev = open(FPGA_TEXT_LCD_DEVICE, O_RDWR);
	if (dev<0) {
		__android_log_print(ANDROID_LOG_INFO, "Device Open Error", "Driver = %d", dev);
		return -1;
	} else {
		strncat(string, "Real Count ", 11);
		str_size=strlen(string);
		memset(string+str_size,' ',LINE_BUFF-str_size);
		str_size=strlen(str);
		if(str_size>0) {
			strncat(string,str,str_size);
			memset(string+LINE_BUFF+str_size,' ',LINE_BUFF-str_size);
		}

		write(dev,string,MAX_BUFF);
		close(dev);
	}
}

int fpga_led(void)
{
	int dev;
	int i;
	unsigned char data;
	unsigned char retval;
	unsigned char val[] = {0x01,0x02,0x04,0x08,0x10,0x20,0x40,0x80};

	dev = open(LED_DEVICE, O_RDWR);
	if (dev<0) {
	} else {
		for(i=7; i>=0; i--) {
			write (dev, &val[i], sizeof(unsigned char));
			sleep(1);
		}
		close(dev);
	}
}

JNIEXPORT jint JNICALL Java_asp_namyun_FBLiker_fragments_LikeListFragment_ReceiveValue( JNIEnv* env,
jobject thiz, jstring val )
{
	jint result;
	const char * str = (*env)->GetStringUTFChars(env,val,NULL);
	__android_log_print(ANDROID_LOG_INFO, "FpgaFndExample", " value = %s", str);
	result=fpga_fnd(str);
	(*env)->ReleaseStringUTFChars(env, val, str);
	return result;
}
