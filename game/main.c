
#include <string.h>
#include <stdio.h>
#include <pd_api.h>
#include <pdcpp/pdnewlib.h>
#include "mylib.h"
#include <stdarg.h>
#include <time.h>
#include <stdbool.h>

#ifndef PLAYDATE_HOLDER
#define PLAYDATE_HOLDER
PlaydateAPI *_pd;
#endif

LCDFont *font;

void pd_scalanative_init(PlaydateAPI *pd)
{
    _pd = pd;
}

static int gameTick(void *userdata)
{
    PlaydateAPI *pd = userdata;

    PDButtons pressed;
    pd->system->getButtonState(NULL, &pressed, NULL);

    // if (pressed & kButtonB)
    // {
    // }

    return sn_update(pd);
};

void log_old_errors(void);
void truncate_errors(void);
void pd_log_error(char *str);

int eventHandler(PlaydateAPI *pd, PDSystemEvent event, uint32_t arg)
{

    // This is required, otherwise linker errors abound
    eventHandler_pdnewlib(pd, event, arg);

    if (event == kEventInit)
    {
        pd_scalanative_init(pd);
        ScalaNativeInit();

        pd->system->setUpdateCallback(gameTick, pd);
    }

    // log_old_errors();

    pd_log_error("eventHandler before terminate check");
    if (event == kEventTerminate)
    {
        _pd->system->logToConsole("App exiting normally, truncating errors...");
        truncate_errors();
    }

    pd_log_error("eventHandler logging event");
    pd->system->logToConsole("Event: %d", event);

    pd_log_error("eventHandler calling sn_event");
    return sn_event(pd, event);
}

// error reporting

void pd_log_error(char *str)
{
    int ts = _pd->system->getCurrentTimeMilliseconds();
    _pd->system->logToConsole("[ERROR][t=%u] %s", ts, str);
    SDFile *file = _pd->file->open("jk-errors.txt", kFileAppend);
    _pd->file->write(file, str, strlen(str));
    _pd->file->write(file, "\n", strlen("\n"));
    _pd->file->close(file);
}

int errno = 0;

int *__error(void)
{
    return &errno;
}

void log_old_errors()
{
    SDFile *file = _pd->file->open("jk-errors.txt", kFileReadData);
    char buf[256];
    int read = 0;
    _pd->system->logToConsole("============== REPLAYING LOGS =================");

    while ((read = _pd->file->read(file, buf, 256)) != 0)
    {
        char str[256 + 1];
        memcpy(str, buf, read);
        str[read] = '\0';
        _pd->system->logToConsole("%s", str);
    }

    _pd->system->logToConsole("============== REPLAY DONE =================");
    _pd->file->close(file);
}

void truncate_errors()
{
    SDFile *file = _pd->file->open("jk-errors.txt", kFileWrite);
    _pd->file->close(file);
}

// PD API forwarders

void pd_system_logToConsole(const char *fmt)
{
    _pd->system->logToConsole(fmt);
}

void pd_display_setRefreshRate(float rate)
{
    _pd->display->setRefreshRate(rate);
}

void pd_system_getButtonState(PDButtons *pressed, PDButtons *held, PDButtons *released)
{
    _pd->system->getButtonState(pressed, held, released);
}

void pd_graphics_fillRect(int x, int y, int w, int h, LCDColor color)
{
    _pd->graphics->fillRect(x, y, w, h, color);
}

void pd_graphics_drawRect(int x, int y, int w, int h, LCDColor color)
{
    _pd->graphics->drawRect(x, y, w, h, color);
}

void pd_graphics_clear(LCDColor color)
{
    _pd->graphics->clear(color);
}

float pd_system_getCrankChange()
{
    return _pd->system->getCrankChange();
}

void pd_system_drawFPS(int x, int y)
{
    _pd->system->drawFPS(x, y);
}

// more polyfills

size_t
scalanative_unwind_sizeof_context()
{
    pd_log_error("scalanative_unwind_sizeof_context");
    exit(19);
    return 0;
}

size_t scalanative_unwind_sizeof_cursor()
{
    pd_log_error("scalanative_unwind_sizeof_cursor");
    exit(20);
    return 0;
}

int scalanative_unwind_init_local(void *cursor, void *context)
{
    pd_log_error("scalanative_unwind_init_local");
    exit(21);
    return 0;
}

int scalanative_unwind_step(void *cursor)
{
    pd_log_error("scalanative_unwind_step");
    exit(22);
    return 0;
}

int scalanative_unw_reg_ip()
{
    pd_log_error("scalanative_unw_reg_ip");
    exit(23);
    return 0;
}

int scalanative_unwind_get_reg(void *cursor, int regnum, size_t *valp)
{
    pd_log_error("scalanative_unwind_get_reg");
    exit(24);
    return 0;
}

int scalanative_unwind_get_context(void *context)
{
    pd_log_error("scalanative_unwind_get_context");
    exit(25);
    return 0;
}

long long scalanative_atomic_load_llong(long long *atm)
{
    pd_log_error("scalanative_atomic_load_llong");
    exit(26);
    return 0;
}

bool scalanative_atomic_compare_exchange_strong_llong(long long *atm, long long *expected, long long desired)
{
    pd_log_error("scalanative_atomic_compare_exchange_strong_llong");
    exit(27);
    return false;
}
