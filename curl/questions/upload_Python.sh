#!/usr/bin/env bash
echo $'\nADD QUESTION' && \
/bin/bash add_question_fileUpload_PYTHON/test.sh && \
echo $'\nEVALUATE QUESTION' && \
/bin/bash evaluate_question_fileUpload/test.sh && \
echo $'\nGET ANSWERS' && \
/bin/bash get_answers_today/test.sh