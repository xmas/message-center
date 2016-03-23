library(sensitivity)
dat <- read.table(paste(dir, "input.dat", sep="/"), sep=",", header=TRUE)
val = mean(as.numeric(dat[0]))
user = "RUser"
templ <- sprintf('[
                          {
                              "title": "Hello, %s",
                              "details": "New value is %s",
                              "path": "Path1",
                              "guid": 0
                          },
                          {
                              "title": "Hello, %s",
                              "details": "New value is %s",
                              "path": "Path2",
                              "guid": 0
                          },
                          {
                              "title": "Hello, %s",
                              "details": "New value is %s",
                              "path": "Path3",
                              "guid": 0
                          }
                      ]', user, val/2, user, val/3, user, val/4)
fileConn<-file(paste(dir, "answers.json", sep = "/"))
writeLines(templ, fileConn)
close(fileConn)
