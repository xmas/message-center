dat <- readLines(paste(dir, "input.dat", sep="/"))
val = mean(as.numeric(dat))
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