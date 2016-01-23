/*
* main.c
* Constructs a Window housing an output TextLayer to show data from
* either modes of operation of the accelerometer.
*/

#include <pebble.h>
#include <limits.h>
#define TAP_NOT_DATA false

static Window *s_main_window;
static TextLayer *s_output_layer;

static int s_uptime = 0;

// for status monitoring

static void outbox_sent_handler(DictionaryIterator *iter, void *context) {
  // Ready for next command
  text_layer_set_text(s_output_layer, "Press up or down.");
}

static void outbox_failed_handler(DictionaryIterator *iter, AppMessageResult reason, void *context) {
  text_layer_set_text(s_output_layer, "Send failed!");
  APP_LOG(APP_LOG_LEVEL_ERROR, "Fail reason: %d", (int)reason);
}



static void select_click_handler(ClickRecognizerRef recognizer, void *context) {
    APP_LOG(APP_LOG_LEVEL_DEBUG, "Select pressed!");
}

static void click_config_provider(void *context) {
    // Register the ClickHandlers
    window_single_click_subscribe(BUTTON_ID_SELECT, select_click_handler);
}

static void tick_handler(struct tm *tick_time, TimeUnits units_changed) {
    // Use a long-lived buffer
    static char s_uptime_buffer[32];
    
    // Get time since launch
    int seconds = s_uptime % 60;
    int minutes = (s_uptime % 3600) / 60;
    int hours = s_uptime / 3600;
    // APP_LOG(APP_LOG_LEVEL_DEBUG, "Uptime: %dh %dm %ds", hours, minutes, seconds);
    
    // Increment s_uptime
    s_uptime++;
}

static void data_handler(AccelData *data, uint32_t num_samples) {
    // Long lived buffer
    static char s_buffer[128];
    static int counter = 0;
    static int ctr = 0;
    static int delX=0, delY=0, delZ=0;
    static int prevY=INT_MAX, prevZ=INT_MIN;
    static int tempY = 0,tempZ=0;
    static bool start = false;
    // Compose string of all data
    if(ctr%1 == 0){
        //if(isButtonPress)
        //{
        
        snprintf(s_buffer, sizeof(s_buffer),
        " %d,%d,%d",
        data[0].x, data[0].y, data[0].z
        
        );
        
     /*   APP_LOG(APP_LOG_LEVEL_DEBUG,s_buffer, sizeof(s_buffer),
        "N X,Y,Z\n0, %d,%d,%d",
        data[0].x, data[0].y, data[0].z);*/
        if(counter==0&& start == false)
        {
            prevY= data[0].y ;
            prevZ=data[0].z;
            start=true;
        }
        if((counter == 0  && (data[0].y<prevY && data[0].z<prevZ)) || ( counter > 0 && counter<15 ) ){
            //|| (prevY>data[0].y && prevZ<data[0].z)
            APP_LOG(APP_LOG_LEVEL_DEBUG,"%d",counter);
            counter++;
        }
        else if(counter == 5 && data[0].y<0 && data[0].z>0){
            tempY = data[0].y;
            tempZ = data[0].z;
            counter++;
        }
        /* else if (counter > 5 && (data[0].y>0 || data[0].z < 0)){
        tempY = 0;
        tempZ = 0;
        counter = 0;
        }*/
        if( counter == 15 && data[0].y>=tempY && data[0].z<=tempZ){
            APP_LOG(APP_LOG_LEVEL_DEBUG,"Flick detected");
            APP_LOG(APP_LOG_LEVEL_DEBUG,"%d",counter);
            counter = 0;
            tempY = 0;
            tempZ=0;
            start = false;
        }
        
        //          prevX=data[0].x;
        prevY=data[0].y;
        prevZ=data[0].z;
        
       
        //}
        
        //\n1 %d,%d,%d\n2 %d,%d,%d
        // data[1].x, data[1].y, data[1].z,
        // data[2].x, data[2].y, data[2].z
        
        
        //Show the data
        text_layer_set_text(s_output_layer, s_buffer);
    }
    //ctr++;
    
}



static void main_window_load(Window *window) {
    Layer *window_layer = window_get_root_layer(window);
    GRect window_bounds = layer_get_bounds(window_layer);
    
    // Create output TextLayer
    s_output_layer = text_layer_create(GRect(5, 0, window_bounds.size.w - 10, window_bounds.size.h));
    text_layer_set_font(s_output_layer, fonts_get_system_font(FONT_KEY_GOTHIC_24));
    text_layer_set_text(s_output_layer, "No data yet.");
    text_layer_set_overflow_mode(s_output_layer, GTextOverflowModeWordWrap);
    layer_add_child(window_layer, text_layer_get_layer(s_output_layer));
}

static void main_window_unload(Window *window) {
    // Destroy output TextLayer
    text_layer_destroy(s_output_layer);
}

static void init() {
    // Create main Window
    s_main_window = window_create();
    window_set_window_handlers(s_main_window, (WindowHandlers) {
        .load = main_window_load,
        .unload = main_window_unload
    });
    ////////////////
    window_set_click_config_provider(s_main_window, click_config_provider);
    ////////////////////////////
    window_stack_push(s_main_window, true);
    
    // Subscribe to the accelerometer data service
    int num_samples = 1;
    accel_data_service_subscribe(num_samples, data_handler);
    
    // Choose update rate
    accel_service_set_sampling_rate(ACCEL_SAMPLING_10HZ);
    tick_timer_service_subscribe(SECOND_UNIT, tick_handler);
    
    window_set_click_config_provider(s_main_window, click_config_provider);
  // Open AppMessage
app_message_open(app_message_inbox_size_maximum(), app_message_outbox_size_maximum());

    
}

static void deinit() {
   
    
    if (TAP_NOT_DATA) {
        accel_tap_service_unsubscribe();
    } else {
        accel_data_service_unsubscribe();
        tick_timer_service_unsubscribe();
        
        
    }
   //Destroy main Window
    window_destroy(s_main_window);
}

int main(void) {
    init();
    app_event_loop();
    deinit();
}