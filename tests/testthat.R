if (packageVersion("testthat") >= "0.7.1.99") {
  library(testthat)
  # temporarily disable testthat tests util the basicTest work
  #test_check("lubridate")
}
