library("lubridate")
library("hamcrest")

test.ymd <- function() {
  dt <- ymd(20101215)
  assertThat(as.character(dt), equalTo("2010-12-15"))
  assertThat(dt, equalTo(as.Date("2010-12-15")))
}

test.mdy <- function() {
  dt <- mdy("4/1/17")
  assertThat(as.character(dt), equalTo("2017-04-01"))
  assertThat(dt, equalTo(as.Date("2017-04-01")))
}

test.month <- function() {
  bday <- dmy("14/10/1979")
  assertThat(bday, equalTo(as.Date("1979-10-14")))
  assertThat(month(bday), equalTo(10))
}

test.wday <- function() {
  bday <- dmy("14-10-1979")
  assertThat(bday, equalTo(as.Date("1979-10-14")))
  assertThat(wday(bday, label = TRUE), equalTo("Sun"))
}

test.setYearWday <- function() {
  bday <- dmy("14.10.1979")
  assertThat(bday, equalTo(as.Date("1979-10-14")))
  year(bday) <- 2016
  assertThat(wday(bday, label = TRUE), equalTo("Fri"))
}

test.ymd_hms <- function() {
  time <- ymd_hms("2010-12-13 15:30:30")
  assertThat(as.character(time), equalTo("2010-12-13 15:30:30"))
  assertThat(time, equalTo(as.POSIXct("2010-12-13 15:30:30", tz = "UTC")))
}

test.withTz <- function() {
  time <- ymd_hms("2010-12-13 15:30:30")
  assertThat(as.character(with_tz(time, "America/Chicago")), equalTo("2010-12-13 09:30:30"))
  act <- with_tz(time, "America/Chicago")
  exp <- as.POSIXct("2010-12-13 09:30:30", tz="America/Chicago")
  assertThat(format(act, format="%Z"), equalTo(format(exp, format="%Z")))
  assertThat(act, equalTo(exp))
}

test.forceTz <- function() {
  time <- ymd_hms("2010-12-13 15:30:30")
  assertThat(as.character(force_tz(time, "America/Chicago")), equalTo("2010-12-13 15:30:30"))
  act <- force_tz(time, "America/Chicago")
  exp <- as.POSIXct("2010-12-13 15:30:30", tz="CST")
  assertThat(format(act, format="%Z"), equalTo(format(exp, format="%Z")))
  exp <- as.POSIXct("2010-12-13 15:30:30", tz="America/Chicago")
  assertThat(act, equalTo(exp))
}